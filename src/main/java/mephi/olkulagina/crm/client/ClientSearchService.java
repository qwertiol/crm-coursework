package mephi.olkulagina.crm.client;

import mephi.olkulagina.crm.company.CompanyService;
import mephi.olkulagina.crm.region.RegionService;
import mephi.olkulagina.crm.search.SearchRequest;
import mephi.olkulagina.crm.specification.ClientSpecification;
import mephi.olkulagina.crm.specification.SpecificationFactory;
import mephi.olkulagina.crm.specification.TrueSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
public class ClientSearchService {

    private static final String NAME_SEARCH_TYPE = "name";
    private static final String COMPANY_SEARCH_TYPE = "company";
    private static final String NAME_PARTS_DELIMITER = "\\s+";
    private static final int NAME_PARTS_LIMIT = 2;

    private final ClientRepository clientRepository;
    private final SpecificationFactory specificationFactory;

    public ClientSearchService(ClientRepository clientRepository,
                               SpecificationFactory specificationFactory) {
        this.clientRepository = clientRepository;
        this.specificationFactory = specificationFactory;
    }

    public Page<Client> search(SearchRequest request, Pageable pageable) {
        List<Client> baseResults = fetchBaseResults(request);
        ClientSpecification filter = buildFilter(request);

        List<Client> filtered = baseResults.stream()
                .filter(filter::isSatisfiedBy)
                .sorted(Comparator.comparing(Client::getLastName,
                        Comparator.nullsLast(String::compareTo))
                        .thenComparing(Client::getFirstName,
                                Comparator.nullsLast(String::compareTo)))
                .collect(Collectors.toList());

        return paginate(filtered, pageable);
    }

    private List<Client> fetchBaseResults(SearchRequest request) {
        String searchType = request.getSearchType();

        if (NAME_SEARCH_TYPE.equals(searchType)) {
            return fetchByName(request.getNameQuery());
        }

        if (COMPANY_SEARCH_TYPE.equals(searchType)) {
            return fetchByCompany(request.getCompanyQuery());
        }

        return clientRepository.findAll();
    }

    private List<Client> fetchByName(String query) {
        if (query == null || query.isBlank()) {
            return clientRepository.findAll();
        }

        String trimmed = query.trim().toLowerCase();

        if (trimmed.contains(" ")) {
            String[] parts = trimmed.split(NAME_PARTS_DELIMITER, NAME_PARTS_LIMIT);
            return clientRepository.findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(parts[0], parts[1]);
        }

        List<Client> byFirst = clientRepository.findByFirstNameContainingIgnoreCase(trimmed);
        List<Client> byLast = clientRepository.findByLastNameContainingIgnoreCase(trimmed);

        return Stream.concat(byFirst.stream(), byLast.stream())
                .distinct()
                .collect(Collectors.toList());
    }

    private List<Client> fetchByCompany(String query) {
        if (query == null || query.isBlank()) {
            return clientRepository.findAll();
        }

        ClientSpecification companySpec = specificationFactory.createCompanyFilter(
                SearchRequest.builder().companyQuery(query).build());

        if (companySpec instanceof TrueSpecification) {
            return List.of();
        }

        return clientRepository.findAll().stream()
                .filter(companySpec::isSatisfiedBy)
                .collect(Collectors.toList());
    }

    private ClientSpecification buildFilter(SearchRequest request) {
        return specificationFactory.createCombinedFilter(request);
    }

    private Page<Client> paginate(List<Client> results, Pageable pageable) {
        int total = results.size();
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), total);

        List<Client> pageContent = (start > total)
                ? List.of()
                : results.subList(start, end);

        return new PageImpl<>(pageContent, pageable, total);
    }
}