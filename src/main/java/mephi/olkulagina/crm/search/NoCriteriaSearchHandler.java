package mephi.olkulagina.crm.search;

import mephi.olkulagina.crm.client.Client;
import mephi.olkulagina.crm.client.ClientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NoCriteriaSearchHandler extends AbstractSearchHandler {
    private final ClientRepository clientRepository;

    public NoCriteriaSearchHandler(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    protected boolean canHandle(SearchRequest request) {
        return true;
    }

    @Override
    protected Page<Client> doSearch(SearchRequest request, Pageable pageable) {
        List<Client> results;
        if (request.getStatusIds() != null && !request.getStatusIds().isEmpty()) {
            results = clientRepository.findByStatusIdIn(request.getStatusIds());
        } else {
            results = clientRepository.findAll();
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