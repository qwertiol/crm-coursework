package mephi.olkulagina.crm.client;

import mephi.olkulagina.crm.company.CompanyService;
import mephi.olkulagina.crm.region.RegionService;
import mephi.olkulagina.crm.status.Status;
import mephi.olkulagina.crm.status.StatusService;
import mephi.olkulagina.crm.validation.ActivityDateValidationStrategy;
import mephi.olkulagina.crm.validation.BirthDateValidationStrategy;
import mephi.olkulagina.crm.validation.EmailValidationStrategy;
import mephi.olkulagina.crm.validation.PhoneValidationStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ClientController {

    private final ClientService clientService;
    private final StatusService statusService;
    private final RegionService regionService;
    private final CompanyService companyService;
    private final EmailValidationStrategy emailValidator;
    private final PhoneValidationStrategy phoneValidator;
    private final BirthDateValidationStrategy birthDateValidator;
    private final ActivityDateValidationStrategy activityDateValidator;

    public ClientController(ClientService clientService, StatusService statusService,
                            RegionService regionService, CompanyService companyService) {
        this.clientService = clientService;
        this.statusService = statusService;
        this.regionService = regionService;
        this.companyService = companyService;
        this.emailValidator = new EmailValidationStrategy();
        this.phoneValidator = new PhoneValidationStrategy();
        this.birthDateValidator = new BirthDateValidationStrategy();
        this.activityDateValidator = new ActivityDateValidationStrategy();
    }

    @GetMapping("/clients")
    public String listClients(Model model) {
        List<Client> clients = clientService.findAll();
        model.addAttribute("clients", clients);
        model.addAttribute("regions", regionService.findAll());
        model.addAttribute("companies", companyService.findAll());
        return "clients";
    }

    @GetMapping("/clients/{id}")
    public String viewClient(@PathVariable Long id, Model model) {
        Client client = clientService.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        model.addAttribute("client", client);
        model.addAttribute("statuses", statusService.findAll());
        return "client-detail";
    }

    @GetMapping("/clients/{id}/edit")
    public String editClient(@PathVariable Long id, Model model) {
        Client client = clientService.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        List<Status> statuses = statusService.findAll();
        model.addAttribute("client", client);
        model.addAttribute("statuses", statuses);
        model.addAttribute("regions", regionService.findAll());
        model.addAttribute("companies", companyService.findAll());
        return "client-edit";
    }

    @PostMapping("/clients/{id}/update")
    public String updateClient(
            @PathVariable Long id,
            @RequestParam String lastName,
            @RequestParam String firstName,
            @RequestParam(required = false) String middleName,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) Long regionId,
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) String birthDate,
            @RequestParam(required = false) String loyaltyCardNumber,
            @RequestParam(required = false) String registrationDate,
            @RequestParam(required = false) String lastActivityDate,
            @RequestParam(required = false) String source,
            RedirectAttributes redirectAttributes,
            Model model) {

        Client client = clientService.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        // Validation
        if (email != null && !email.isEmpty() && !emailValidator.isValid(email)) {
            model.addAttribute("client", client);
            model.addAttribute("statuses", statusService.findAll());
            model.addAttribute("regions", regionService.findAll());
            model.addAttribute("companies", companyService.findAll());
            model.addAttribute("errorMessage", emailValidator.getErrorMessage());
            return "client-edit";
        }

        if (phone != null && !phone.isEmpty() && !phoneValidator.isValid(phone)) {
            model.addAttribute("client", client);
            model.addAttribute("statuses", statusService.findAll());
            model.addAttribute("regions", regionService.findAll());
            model.addAttribute("companies", companyService.findAll());
            model.addAttribute("errorMessage", phoneValidator.getErrorMessage());
            return "client-edit";
        }

        if (birthDate != null && !birthDate.isEmpty() && !birthDateValidator.isValid(birthDate)) {
            model.addAttribute("client", client);
            model.addAttribute("statuses", statusService.findAll());
            model.addAttribute("regions", regionService.findAll());
            model.addAttribute("companies", companyService.findAll());
            model.addAttribute("errorMessage", birthDateValidator.getErrorMessage());
            return "client-edit";
        }

        if (registrationDate != null && !registrationDate.isEmpty() && !activityDateValidator.isValid(registrationDate)) {
            model.addAttribute("client", client);
            model.addAttribute("statuses", statusService.findAll());
            model.addAttribute("regions", regionService.findAll());
            model.addAttribute("companies", companyService.findAll());
            model.addAttribute("errorMessage", activityDateValidator.getErrorMessage());
            return "client-edit";
        }

        if (lastActivityDate != null && !lastActivityDate.isEmpty() && !activityDateValidator.isValid(lastActivityDate)) {
            model.addAttribute("client", client);
            model.addAttribute("statuses", statusService.findAll());
            model.addAttribute("regions", regionService.findAll());
            model.addAttribute("companies", companyService.findAll());
            model.addAttribute("errorMessage", activityDateValidator.getErrorMessage());
            return "client-edit";
        }

        // Apply changes
        client.setLastName(lastName);
        client.setFirstName(firstName);
        if (middleName != null) client.setMiddleName(middleName);
        if (phone != null) client.setPhone(phone);
        if (email != null) client.setEmail(email);
        if (gender != null && !gender.isEmpty()) client.setGender(Gender.valueOf(gender));
        if (regionId != null) client.setRegion(regionService.findById(regionId).orElse(null));
        if (companyId != null) client.setCompany(companyService.findById(companyId).orElse(null));
        if (birthDate != null && !birthDate.isEmpty()) client.setBirthDate(LocalDate.parse(birthDate));
        if (loyaltyCardNumber != null) client.setLoyaltyCardNumber(loyaltyCardNumber);
        if (registrationDate != null && !registrationDate.isEmpty()) client.setRegistrationDate(LocalDate.parse(registrationDate));
        if (lastActivityDate != null && !lastActivityDate.isEmpty()) client.setLastActivityDate(LocalDate.parse(lastActivityDate));
        if (source != null && !source.isEmpty()) client.setSource(ClientSource.valueOf(source));

        try {
            clientService.save(client);
            redirectAttributes.addFlashAttribute("successMessage", "Changes saved successfully");
            return "redirect:/clients/" + id;
        } catch (Exception e) {
            model.addAttribute("client", client);
            model.addAttribute("statuses", statusService.findAll());
            model.addAttribute("regions", regionService.findAll());
            model.addAttribute("companies", companyService.findAll());
            model.addAttribute("errorMessage", "Failed to save changes: " + e.getMessage());
            return "client-edit";
        }
    }

    @PostMapping("/clients/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam Long statusId,
                               RedirectAttributes redirectAttributes) {
        try {
            Client client = clientService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Client not found"));
            Status status = statusService.findById(statusId)
                    .orElseThrow(() -> new RuntimeException("Status not found"));
            client.setStatus(status);
            clientService.save(client);
            redirectAttributes.addFlashAttribute("successMessage", "Status updated successfully");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/clients/" + id;
    }
}