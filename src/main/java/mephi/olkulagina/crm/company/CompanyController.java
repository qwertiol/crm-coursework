package mephi.olkulagina.crm.company;

import mephi.olkulagina.crm.company.lookup.CompanyLookupService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyLookupService companyLookupService;

    public CompanyController(CompanyLookupService companyLookupService) {
        this.companyLookupService = companyLookupService;
    }

    @GetMapping("/search")
    public List<Company> search(@RequestParam("q") String query) {
        return companyLookupService.findMatching(query);
    }
}