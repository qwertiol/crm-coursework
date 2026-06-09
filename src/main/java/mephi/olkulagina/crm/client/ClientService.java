package mephi.olkulagina.crm.client;

import mephi.olkulagina.crm.company.Company;
import mephi.olkulagina.crm.company.CompanyService;
import mephi.olkulagina.crm.region.Region;
import mephi.olkulagina.crm.region.RegionService;
import mephi.olkulagina.crm.status.Status;
import mephi.olkulagina.crm.status.StatusRepository;
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

    private static final int MIN_NAME_LENGTH = 1;

    private final ClientRepository clientRepository;
    private final StatusRepository statusRepository;
    private final CompanyService companyService;
    private final RegionService regionService;
    private final EmailValidationStrategy emailValidator;
    private final PhoneValidationStrategy phoneValidator;
    private final ClientDatesValidationStrategy clientDatesValidator;

    public ClientService(ClientRepository clientRepository, StatusRepository statusRepository,
                         CompanyService companyService, RegionService regionService) {
        this.clientRepository = clientRepository;
        this.statusRepository = statusRepository;
        this.companyService = companyService;
        this.regionService = regionService;
        this.emailValidator = new EmailValidationStrategy();
        this.phoneValidator = new PhoneValidationStrategy();
        this.clientDatesValidator = new ClientDatesValidationStrategy();
    }

    @Transactional(readOnly = true)
    public Optional<Client> findById(Long id) {
        return clientRepository.findById(id);
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

    private void validateAllInputs(String lastName, String firstName, String email, String phone,
                                   String clientLevel, String registrationDate, String lastActivityDate,
                                   String source, Long statusId) {
        if (lastName == null || lastName.length() < MIN_NAME_LENGTH) {
            throw new RuntimeException("Last name is required");
        }
        if (firstName == null || firstName.length() < MIN_NAME_LENGTH) {
            throw new RuntimeException("First name is required");
        }

        List<String> errors = validateClientData(email, phone);
        if (!errors.isEmpty()) {
            throw new RuntimeException(String.join(", ", errors));
        }

        if (registrationDate != null && !registrationDate.isEmpty()) {
            if (!clientDatesValidator.isValid(registrationDate)) {
                throw new RuntimeException("Field 'registrationDate': " + clientDatesValidator.getErrorMessage());
            }
        }
        if (lastActivityDate != null && !lastActivityDate.isEmpty()) {
            if (!clientDatesValidator.isValid(lastActivityDate)) {
                throw new RuntimeException("Field 'lastActivityDate': " + clientDatesValidator.getErrorMessage());
            }
        }

        if (clientLevel != null && !clientLevel.isEmpty()) {
            try {
                ClientLevel.valueOf(clientLevel);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid client level: " + clientLevel);
            }
        }
        if (source != null && !source.isEmpty()) {
            try {
                ClientSource.valueOf(source);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid source: " + source);
            }
        }

        if (statusId != null) {
            statusRepository.findById(statusId)
                    .orElseThrow(() -> new RuntimeException("Status not found"));
        }
    }

    public Client updateClient(Long id, String lastName, String firstName, String middleName,
                               String phone, String email, String position, String department,
                               String clientLevel, String specialConditions, String companyName,
                               String regionName, String registrationDate, String lastActivityDate,
                               String source, Long statusId) {

        validateAllInputs(lastName, firstName, email, phone, clientLevel,
                registrationDate, lastActivityDate, source, statusId);

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

        Company company = companyService.findOrCreate(companyName);
        client.setCompany(company);

        Region region = regionService.findOrCreate(regionName);
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