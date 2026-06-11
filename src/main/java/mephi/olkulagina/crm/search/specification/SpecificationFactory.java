package mephi.olkulagina.crm.search.specification;

import mephi.olkulagina.crm.search.SearchRequest;
import mephi.olkulagina.crm.company.Company;
import mephi.olkulagina.crm.company.CompanyRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class SpecificationFactory {

    private final CompanyRepository companyRepository;

    public SpecificationFactory(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public ClientSpecification createStatusFilter(SearchRequest request) {
        List<Long> statusIds = request.getStatusIds();
        if (statusIds == null || statusIds.isEmpty()) {
            return new TrueSpecification();
        }
        return new StatusSpecification(statusIds);
    }

    public ClientSpecification createNameFilter(SearchRequest request) {
        String nameQuery = request.getNameQuery();
        if (nameQuery == null || nameQuery.isBlank()) {
            return new TrueSpecification();
        }
        return new NameSpecification(nameQuery);
    }

    public ClientSpecification createCompanyFilter(SearchRequest request) {
        String companyQuery = request.getCompanyQuery();
        if (companyQuery == null || companyQuery.isBlank()) {
            return new TrueSpecification();
        }

        List<Company> companies = companyRepository.findByNameContainingIgnoreCase(companyQuery.trim());
        if (companies.isEmpty()) {
            return client -> false;
        }

        List<Long> companyIds = companies.stream()
                .map(Company::getId)
                .toList();

        return new CompanySpecification(companyIds);
    }

    public ClientSpecification createCombinedFilter(SearchRequest request) {
        ClientSpecification result = new TrueSpecification();

        ClientSpecification statusSpec = createStatusFilter(request);
        if (!(statusSpec instanceof TrueSpecification)) {
            result = result.and(statusSpec);
        }

        return result;
    }
}