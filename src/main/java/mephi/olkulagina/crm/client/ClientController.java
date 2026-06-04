package mephi.olkulagina.crm.client;

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

    public ClientController(ClientService clientService, StatusService statusService) {
        this.clientService = clientService;
        this.statusService = statusService;
    }

    @GetMapping("/clients")
    public String listClients(Model model) {
        List<Client> clients = clientService.findAll();
        model.addAttribute("clients", clients);
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
        List<Status> statuses = statusService.findAll();
        model.addAttribute("client", client);
        model.addAttribute("statuses", statuses);
        return "client-edit";
    }

    @PostMapping("/clients/{id}/update")
    public String updateClient(
            @PathVariable Long id,
            @RequestParam String fullName,
            @RequestParam String phone,
            @RequestParam String company,
            @RequestParam Long statusId,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String birthDate,
            @RequestParam(required = false) String loyaltyCardNumber,
            RedirectAttributes redirectAttributes) {
        Client client = clientService.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        client.setFullName(fullName);
        client.setPhone(phone);
        client.setCompany(company);
        client.setEmail(email);
        client.setRegion(region);
        if (birthDate != null && !birthDate.isEmpty()) {
            client.setBirthDate(LocalDate.parse(birthDate));
        }
        client.setLoyaltyCardNumber(loyaltyCardNumber);
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