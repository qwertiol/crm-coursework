package mephi.olkulagina.crm.company.lookup;

import mephi.olkulagina.crm.company.Company;
import mephi.olkulagina.crm.company.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyLookupServiceImpl implements CompanyLookupService {
    private final CompanyRepository companyRepository;

    public CompanyLookupServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    @Override
    public List<Company> findMatching(String query) {
        if (query == null || query.isBlank()) {
            return List.of();
        }
        return companyRepository.findByNameContainingIgnoreCase(query.trim());
    }
}