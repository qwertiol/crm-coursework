package mephi.olkulagina.crm.client;

import mephi.olkulagina.crm.company.Company;
import mephi.olkulagina.crm.company.CompanyService;
import mephi.olkulagina.crm.region.Region;
import mephi.olkulagina.crm.region.RegionService;
import mephi.olkulagina.crm.status.Status;
import mephi.olkulagina.crm.status.StatusRepository;
import mephi.olkulagina.crm.validation.BirthDateValidationStrategy;
import mephi.olkulagina.crm.validation.ClientDatesValidationStrategy;
import mephi.olkulagina.crm.validation.EmailValidationStrategy;
import mephi.olkulagina.crm.validation.PhoneValidationStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClientService {

    private final ClientRepository clientRepository;
    private final StatusRepository statusRepository;
    private final RegionService regionService;
    private final CompanyService companyService;
    private final EmailValidationStrategy emailValidator;
    private final PhoneValidationStrategy phoneValidator;
    private final BirthDateValidationStrategy birthDateValidator;
    private final ClientDatesValidationStrategy clientDatesValidator;

    public ClientService(ClientRepository clientRepository, StatusRepository statusRepository,
                         RegionService regionService, CompanyService companyService) {
        this.clientRepository = clientRepository;
        this.statusRepository = statusRepository;
        this.regionService = regionService;
        this.companyService = companyService;
        this.emailValidator = new EmailValidationStrategy();
        this.phoneValidator = new PhoneValidationStrategy();
        this.birthDateValidator = new BirthDateValidationStrategy();
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
    public List<Client> findByGender(Gender gender) {
        return clientRepository.findByGender(gender);
    }

    @Transactional(readOnly = true)
    public List<Client> findBySource(ClientSource source) {
        return clientRepository.findBySource(source);
    }

    @Transactional(readOnly = true)
    public List<Client> findByStatusId(Long statusId) {
        return clientRepository.findByStatusId(statusId);
    }

    public List<String> validateClientData(String email, String phone, String birthDate, String registrationDate, String lastActivityDate) {
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

        if (birthDate != null && !birthDate.isEmpty()) {
            if (!birthDateValidator.isValid(birthDate)) {
                errors.add("Field 'birthDate': " + birthDateValidator.getErrorMessage());
            }
        }

        if (registrationDate != null && !registrationDate.isEmpty()) {
            if (!clientDatesValidator.isValid(registrationDate)) {
                errors.add("Field 'registrationDate': " + clientDatesValidator.getErrorMessage());
            }
        }

        if (lastActivityDate != null && !lastActivityDate.isEmpty()) {
            if (!clientDatesValidator.isValid(lastActivityDate)) {
                errors.add("Field 'lastActivityDate': " + clientDatesValidator.getErrorMessage());
            }
        }

        return errors;
    }

    public Client updateClient(Long id, String lastName, String firstName, String middleName,
                               String phone, String email, String gender, Long regionId, Long companyId,
                               String birthDate, String loyaltyCardNumber, String registrationDate,
                               String lastActivityDate, String source, Long statusId) {
        if (lastName == null || lastName.isEmpty()) {
            throw new RuntimeException("Last name is required");
        }
        if (firstName == null || firstName.isEmpty()) {
            throw new RuntimeException("First name is required");
        }

        List<String> errors = validateClientData(email, phone, birthDate, registrationDate, lastActivityDate);
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
        if (gender != null && !gender.isEmpty()) {
            client.setGender(Gender.valueOf(gender));
        }
        if (regionId != null) {
            Region region = regionService.findById(regionId)
                    .orElseThrow(() -> new RuntimeException("Region not found"));
            client.setRegion(region);
        }
        if (companyId != null) {
            Company company = companyService.findById(companyId)
                    .orElseThrow(() -> new RuntimeException("Company not found"));
            client.setCompany(company);
        }
        if (birthDate != null && !birthDate.isEmpty()) {
            client.setBirthDate(LocalDate.parse(birthDate));
        }
        if (loyaltyCardNumber != null) {
            client.setLoyaltyCardNumber(loyaltyCardNumber);
        }
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