package mephi.olkulagina.crm.search;

import mephi.olkulagina.crm.client.Client;
import mephi.olkulagina.crm.client.ClientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NameSearchHandler extends AbstractSearchHandler {
    private final ClientRepository clientRepository;

    public NameSearchHandler(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    protected boolean canHandle(SearchRequest request) {
        return "name".equals(request.getSearchType())
                && request.getNameQuery() != null
                && !request.getNameQuery().isBlank();
    }

    @Override
    protected Page<Client> doSearch(SearchRequest request, Pageable pageable) {
        String query = request.getNameQuery().trim();
        List<Client> results;

        if (query.contains(" ")) {
            String[] parts = query.split("\\s+", 2);
            String first = parts[0];
            String last = parts[1];
            results = clientRepository.findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(first, last);
        } else {
            List<Client> byFirst = clientRepository.findByFirstNameContainingIgnoreCase(query);
            List<Client> byLast = clientRepository.findByLastNameContainingIgnoreCase(query);
            results = Stream.concat(byFirst.stream(), byLast.stream())
                    .collect(Collectors.toMap(Client::getId, c -> c, (a, b) -> a))
                    .values()
                    .stream()
                    .collect(Collectors.toList());
        }

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