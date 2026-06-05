package mephi.olkulagina.crm.client;

import mephi.olkulagina.crm.company.CompanyRepository;
import mephi.olkulagina.crm.company.CompanyService;
import mephi.olkulagina.crm.region.RegionService;
import mephi.olkulagina.crm.search.*;
import mephi.olkulagina.crm.status.StatusService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;

@Controller
public class ClientController {

    private final ClientService clientService;
    private final StatusService statusService;
    private final RegionService regionService;
    private final CompanyService companyService;
    private final ClientRepository clientRepository;
    private final CompanyRepository companyRepository;

    public ClientController(ClientService clientService, StatusService statusService,
                            RegionService regionService, CompanyService companyService,
                            ClientRepository clientRepository, CompanyRepository companyRepository) {
        this.clientService = clientService;
        this.statusService = statusService;
        this.regionService = regionService;
        this.companyService = companyService;
        this.clientRepository = clientRepository;
        this.companyRepository = companyRepository;
    }

    @GetMapping("/clients")
    public String listClients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size,
            @RequestParam(required = false) List<Long> statusIds,
            @RequestParam(required = false) String nameQuery,
            @RequestParam(required = false) String companyQuery,
            Model model) {

        PageRequest pageable = PageRequest.of(page, size, Sort.by("lastName").ascending());

        SearchRequest request = new SearchRequest();
        request.setNameQuery(nameQuery);
        request.setCompanyQuery(companyQuery);
        request.setStatusIds(statusIds);

        CombinedSearchHandler combined = new CombinedSearchHandler(clientRepository, companyRepository);
        NameSearchHandler nameHandler = new NameSearchHandler(clientRepository);
        CompanySearchHandler companyHandler = new CompanySearchHandler(clientRepository, companyRepository);
        NoCriteriaSearchHandler noCriteria = new NoCriteriaSearchHandler(clientRepository);

        combined.setNext(nameHandler);
        nameHandler.setNext(companyHandler);
        companyHandler.setNext(noCriteria);

        Page<Client> clientPage = combined.handle(request, pageable);

        model.addAttribute("clients", clientPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", clientPage.getTotalPages());
        model.addAttribute("totalElements", clientPage.getTotalElements());
        model.addAttribute("statuses", statusService.findAll());
        model.addAttribute("selectedStatusIds", statusIds != null ? statusIds : Collections.emptyList());
        model.addAttribute("resultsCount", clientPage.getTotalElements());
        model.addAttribute("nameQuery", nameQuery);
        model.addAttribute("companyQuery", companyQuery);

        if (statusIds != null && !statusIds.isEmpty()) {
            model.addAttribute("filterMessage", "Showing " + clientPage.getTotalElements() + " clients filtered by selected statuses");
        }

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
        List<mephi.olkulagina.crm.status.Status> statuses = statusService.findAll();
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
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String clientLevel,
            @RequestParam(required = false) String specialConditions,
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) String regionName,
            @RequestParam(required = false) String registrationDate,
            @RequestParam(required = false) String lastActivityDate,
            @RequestParam(required = false) String source,
            RedirectAttributes redirectAttributes) {
        try {
            clientService.updateClient(id, lastName, firstName, middleName, phone, email,
                    position, department, clientLevel, specialConditions, companyName,
                    regionName, registrationDate, lastActivityDate, source, null);
            redirectAttributes.addFlashAttribute("successMessage", "Changes saved successfully");
            return "redirect:/clients/" + id;
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/clients/" + id + "/edit";
        }
    }

    @PostMapping("/clients/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam Long statusId,
                               RedirectAttributes redirectAttributes) {
        try {
            clientService.quickStatusChange(id, statusId);
            redirectAttributes.addFlashAttribute("successMessage", "Status updated successfully");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/clients/" + id;
    }
}