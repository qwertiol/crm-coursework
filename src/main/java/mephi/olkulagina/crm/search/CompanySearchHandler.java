package mephi.olkulagina.crm.search;

import mephi.olkulagina.crm.client.Client;
import mephi.olkulagina.crm.client.ClientRepository;
import mephi.olkulagina.crm.company.Company;
import mephi.olkulagina.crm.company.CompanyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CompanySearchHandler extends AbstractSearchHandler {
    private final ClientRepository clientRepository;
    private final CompanyRepository companyRepository;

    public CompanySearchHandler(ClientRepository clientRepository, CompanyRepository companyRepository) {
        this.clientRepository = clientRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    protected boolean canHandle(SearchRequest request) {
        return "company".equals(request.getSearchType())
                && request.getCompanyQuery() != null
                && !request.getCompanyQuery().isBlank();
    }

    @Override
    protected Page<Client> doSearch(SearchRequest request, Pageable pageable) {
        String query = request.getCompanyQuery().trim();
        List<Company> companies = companyRepository.findByNameContainingIgnoreCase(query);
        if (companies.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }
        List<Long> companyIds = companies.stream().map(Company::getId).collect(Collectors.toList());
        List<Client> results = clientRepository.findByCompanyIdIn(companyIds);

        if (request.getStatusIds() != null && !request.getStatusIds().isEmpty()) {
            results = results.stream()
                    .filter(c -> c.getStatus() != null && request.getStatusIds().contains(c.getStatus().getId()))
                    .collect(Collectors.toList());
        }

        results.sort(Comparator.comparing(Client::getLastName, Comparator.nullsLast(String::compareTo))
                .thenComparing(Client::getFirstName, Comparator.nullsLast(String::compareTo)));

        int total = results.size();
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), total);
        List<Client> pageContent = start > total ? Collections.emptyList() : results.subList(start, end);
        return new PageImpl<>(pageContent, pageable, total);
    }
}