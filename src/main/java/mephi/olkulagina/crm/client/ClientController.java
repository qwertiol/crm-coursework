package mephi.olkulagina.crm.client;

import mephi.olkulagina.crm.company.CompanyService;
import mephi.olkulagina.crm.region.RegionService;
import mephi.olkulagina.crm.status.Status;
import mephi.olkulagina.crm.status.StatusService;
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

    public ClientController(ClientService clientService, StatusService statusService,
                            RegionService regionService, CompanyService companyService) {
        this.clientService = clientService;
        this.statusService = statusService;
        this.regionService = regionService;
        this.companyService = companyService;
    }

    @GetMapping("/clients")
    public String listClients(Model model) {
        model.addAttribute("clients", clientService.findAll());
        model.addAttribute("regions", regionService.findAll());
        model.addAttribute("companies", companyService.findAll());
        return "clients";
    }

    @GetMapping("/clients/{id}")
    public String viewClient(@PathVariable Long id, Model model) {
        Client client = clientService.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        model.addAttribute("client", client);
        return "client-detail";
    }

    @GetMapping("/clients/{id}/edit")
    public String editClient(@PathVariable Long id, Model model) {
        Client client = clientService.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        model.addAttribute("client", client);
        model.addAttribute("statuses", statusService.findAll());
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
            @RequestParam Long statusId,
            RedirectAttributes redirectAttributes) {
        Client client = clientService.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));
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
        Status status = statusService.findById(statusId)
                .orElseThrow(() -> new RuntimeException("Status not found"));
        client.setStatus(status);
        try {
            clientService.save(client);
            redirectAttributes.addFlashAttribute("successMessage", "Changes saved successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to save changes: " + e.getMessage());
        }
        return "redirect:/clients/" + id;
    }
}