package mephi.olkulagina.crm.search;

import mephi.olkulagina.crm.client.Client;
import mephi.olkulagina.crm.client.ClientRepository;
import mephi.olkulagina.crm.company.CompanyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CombinedSearchHandler extends AbstractSearchHandler {
    private final ClientRepository clientRepository;
    private final CompanyRepository companyRepository;

    public CombinedSearchHandler(ClientRepository clientRepository, CompanyRepository companyRepository) {
        this.clientRepository = clientRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    protected boolean canHandle(SearchRequest request) {
        return request.getNameQuery() != null && !request.getNameQuery().isBlank()
                && request.getCompanyQuery() != null && !request.getCompanyQuery().isBlank();
    }

    @Override
    protected Page<Client> doSearch(SearchRequest request, Pageable pageable) {
        NameSearchHandler nameHandler = new NameSearchHandler(clientRepository);
        CompanySearchHandler companyHandler = new CompanySearchHandler(clientRepository, companyRepository);

        Page<Client> nameResults = nameHandler.doSearch(request, Pageable.unpaged());
        Page<Client> companyResults = companyHandler.doSearch(request, Pageable.unpaged());

        Set<Long> companyResultIds = companyResults.getContent().stream()
                .map(Client::getId)
                .collect(Collectors.toSet());

        List<Client> combined = nameResults.getContent().stream()
                .filter(c -> companyResultIds.contains(c.getId()))
                .collect(Collectors.toList());

        if (request.getStatusIds() != null && !request.getStatusIds().isEmpty()) {
            combined = combined.stream()
                    .filter(c -> c.getStatus() != null && request.getStatusIds().contains(c.getStatus().getId()))
                    .collect(Collectors.toList());
        }

        combined.sort(Comparator.comparing(Client::getLastName, Comparator.nullsLast(String::compareTo))
                .thenComparing(Client::getFirstName, Comparator.nullsLast(String::compareTo)));

        int total = combined.size();
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), total);
        List<Client> pageContent = start > total ? Collections.emptyList() : combined.subList(start, end);
        return new PageImpl<>(pageContent, pageable, total);
    }
}