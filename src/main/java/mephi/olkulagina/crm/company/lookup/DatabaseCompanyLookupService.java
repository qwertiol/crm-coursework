package mephi.olkulagina.crm.company.lookup;

import mephi.olkulagina.crm.company.Company;
import mephi.olkulagina.crm.company.CompanyRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatabaseCompanyLookupService implements CompanyLookupService {
    
    private final CompanyRepository companyRepository;
    
    public DatabaseCompanyLookupService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }
    
    @Override
    @Cacheable(value = "companies", unless = "#result.isEmpty()")
    public List<Company> findAll() {
        return companyRepository.findAll();
    }
    
    @Override
    @Cacheable(value = "companySearch", key = "#query.toLowerCase()", unless = "#result.isEmpty()")
    public List<Company> findMatching(String query) {
        if (query == null || query.isBlank()) {
            return List.of();
        }
        return companyRepository.findByNameContainingIgnoreCase(query.trim());
    }
}