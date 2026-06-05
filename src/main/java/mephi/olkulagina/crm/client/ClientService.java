package mephi.olkulagina.crm.client;

import mephi.olkulagina.crm.company.Company;
import mephi.olkulagina.crm.company.CompanyRepository;
import mephi.olkulagina.crm.region.Region;
import mephi.olkulagina.crm.region.RegionRepository;
import mephi.olkulagina.crm.status.Status;
import mephi.olkulagina.crm.status.StatusRepository;
import mephi.olkulagina.crm.validation.ClientDatesValidationStrategy;
import mephi.olkulagina.crm.validation.EmailValidationStrategy;
import mephi.olkulagina.crm.validation.PhoneValidationStrategy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClientService {

    private final ClientRepository clientRepository;
    private final StatusRepository statusRepository;
    private final CompanyRepository companyRepository;
    private final RegionRepository regionRepository;
    private final EmailValidationStrategy emailValidator;
    private final PhoneValidationStrategy phoneValidator;
    private final ClientDatesValidationStrategy clientDatesValidator;

    public ClientService(ClientRepository clientRepository, StatusRepository statusRepository,
                         CompanyRepository companyRepository, RegionRepository regionRepository) {
        this.clientRepository = clientRepository;
        this.statusRepository = statusRepository;
        this.companyRepository = companyRepository;
        this.regionRepository = regionRepository;
        this.emailValidator = new EmailValidationStrategy();
        this.phoneValidator = new PhoneValidationStrategy();
        this.clientDatesValidator = new ClientDatesValidationStrategy();
    }

    @Transactional(readOnly = true)
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Client> findById(Long id) {
        return clientRepository.findById(id);
    }

    public Client save(Client client) {
        return clientRepository.save(client);
    }

    public Client updateStatus(Long clientId, Long statusId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + clientId));
        Status status = statusRepository.findById(statusId)
                .orElseThrow(() -> new RuntimeException("Status not found with id: " + statusId));
        client.setStatus(status);
        return clientRepository.save(client);
    }

    @Transactional(readOnly = true)
    public List<Client> findByLastNameContainingIgnoreCase(String lastName) {
        return clientRepository.findByLastNameContainingIgnoreCase(lastName);
    }

    @Transactional(readOnly = true)
    public List<Client> findByFirstNameContainingIgnoreCase(String firstName) {
        return clientRepository.findByFirstNameContainingIgnoreCase(firstName);
    }

    @Transactional(readOnly = true)
    public List<Client> findByEmailContainingIgnoreCase(String email) {
        return clientRepository.findByEmailContainingIgnoreCase(email);
    }

    @Transactional(readOnly = true)
    public List<Client> findByPhoneContaining(String phone) {
        return clientRepository.findByPhoneContaining(phone);
    }

    @Transactional(readOnly = true)
    public List<Client> findByRegionId(Long regionId) {
        return clientRepository.findByRegionId(regionId);
    }

    @Transactional(readOnly = true)
    public List<Client> findByCompanyId(Long companyId) {
        return clientRepository.findByCompanyId(companyId);
    }

    @Transactional(readOnly = true)
    public List<Client> findByStatusId(Long statusId) {
        return clientRepository.findByStatusId(statusId);
    }

    @Transactional(readOnly = true)
    public Page<Client> findClients(List<Long> statusIds, Pageable pageable) {
        List<Client> allClients;
        if (statusIds == null || statusIds.isEmpty()) {
            allClients = clientRepository.findAll();
        } else {
            allClients = clientRepository.findByStatusIdIn(statusIds);
        }

        int total = allClients.size();
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), total);

        List<Client> pageContent;
        if (start > total) {
            pageContent = Collections.emptyList();
        } else {
            pageContent = allClients.subList(start, end);
        }

        return new PageImpl<>(pageContent, pageable, total);
    }

    @Transactional(readOnly = true)
    public long countClients(List<Long> statusIds) {
        if (statusIds == null || statusIds.isEmpty()) {
            return clientRepository.count();
        }
        return clientRepository.findByStatusIdIn(statusIds).size();
    }

    public Company findOrCreateCompany(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        String trimmed = name.trim();
        return companyRepository.findByName(trimmed)
                .orElseGet(() -> companyRepository.save(new Company(null, trimmed)));
    }

    public Region findOrCreateRegion(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        String trimmed = name.trim();
        return regionRepository.findByName(trimmed)
                .orElseGet(() -> regionRepository.save(new Region(null, trimmed)));
    }

    public List<String> validateClientData(String email, String phone) {
        List<String> errors = new ArrayList<>();

        if (email != null && !email.isEmpty()) {
            if (!emailValidator.isValid(email)) {
                errors.add("Field 'email': " + emailValidator.getErrorMessage());
            }
        }

        if (phone != null && !phone.isEmpty()) {
            if (!phoneValidator.isValid(phone)) {
                errors.add("Field 'phone': " + phoneValidator.getErrorMessage());
            }
        }

        return errors;
    }

    public Client updateClient(Long id, String lastName, String firstName, String middleName,
                               String phone, String email, String position, String department,
                               String clientLevel, String specialConditions, String companyName,
                               String regionName, String registrationDate, String lastActivityDate,
                               String source, Long statusId) {
        if (lastName == null || lastName.isEmpty()) {
            throw new RuntimeException("Last name is required");
        }
        if (firstName == null || firstName.isEmpty()) {
            throw new RuntimeException("First name is required");
        }

        List<String> errors = validateClientData(email, phone);
        if (!errors.isEmpty()) {
            throw new RuntimeException(String.join(", ", errors));
        }

        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        client.setLastName(lastName);
        client.setFirstName(firstName);
        if (middleName != null) {
            client.setMiddleName(middleName);
        }
        if (phone != null) {
            client.setPhone(phone);
        }
        if (email != null) {
            client.setEmail(email);
        }
        if (position != null) {
            client.setPosition(position);
        }
        if (department != null) {
            client.setDepartment(department);
        }
        if (clientLevel != null && !clientLevel.isEmpty()) {
            client.setClientLevel(ClientLevel.valueOf(clientLevel));
        }
        if (specialConditions != null) {
            client.setSpecialConditions(specialConditions);
        }

        Company company = findOrCreateCompany(companyName);
        client.setCompany(company);

        Region region = findOrCreateRegion(regionName);
        client.setRegion(region);

        if (registrationDate != null && !registrationDate.isEmpty()) {
            client.setRegistrationDate(LocalDate.parse(registrationDate));
        }
        if (lastActivityDate != null && !lastActivityDate.isEmpty()) {
            client.setLastActivityDate(LocalDate.parse(lastActivityDate));
        }
        if (source != null && !source.isEmpty()) {
            client.setSource(ClientSource.valueOf(source));
        }
        if (statusId != null) {
            Status status = statusRepository.findById(statusId)
                    .orElseThrow(() -> new RuntimeException("Status not found"));
            client.setStatus(status);
        }

        return clientRepository.save(client);
    }

    public Client quickStatusChange(Long clientId, Long statusId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        Status status = statusRepository.findById(statusId)
                .orElseThrow(() -> new RuntimeException("Status not found"));
        client.setStatus(status);
        return clientRepository.save(client);
    }
}