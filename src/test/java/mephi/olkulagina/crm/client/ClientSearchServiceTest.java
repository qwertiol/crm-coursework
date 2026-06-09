package mephi.olkulagina.crm.client;

import mephi.olkulagina.crm.search.SearchRequest;
import mephi.olkulagina.crm.specification.SpecificationFactory;
import mephi.olkulagina.crm.specification.TrueSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientSearchServiceTest {

    @Mock private ClientRepository clientRepository;
    @Mock private SpecificationFactory specificationFactory;

    @InjectMocks
    private ClientSearchService clientSearchService;

    @Test
    void shouldSearchByNameWithSingleWord() {
        Client c1 = new Client(); c1.setFirstName("Alice"); c1.setLastName("Smith");
        when(clientRepository.findByFirstNameContainingIgnoreCase("ali")).thenReturn(List.of(c1));
        when(clientRepository.findByLastNameContainingIgnoreCase("ali")).thenReturn(List.of());
        when(specificationFactory.createCombinedFilter(any())).thenReturn(new TrueSpecification());

        SearchRequest req = SearchRequest.builder().nameQuery("ali").searchType("name").build();
        Page<Client> page = clientSearchService.search(req, PageRequest.of(0, 10));

        assertEquals(1, page.getTotalElements());
        assertEquals("Alice", page.getContent().get(0).getFirstName());
    }

    @Test
    void shouldSearchByNameWithTwoWords() {
        Client c1 = new Client(); c1.setFirstName("John"); c1.setLastName("Doe");
        when(clientRepository.findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase("john", "doe"))
            .thenReturn(List.of(c1));
        when(specificationFactory.createCombinedFilter(any())).thenReturn(new TrueSpecification());

        SearchRequest req = SearchRequest.builder().nameQuery("John Doe").searchType("name").build();
        Page<Client> page = clientSearchService.search(req, PageRequest.of(0, 10));

        assertEquals(1, page.getTotalElements());
    }

    @Test
    void shouldReturnAllWhenNameQueryIsBlank() {
        when(clientRepository.findAll()).thenReturn(List.of(new Client(), new Client()));
        when(specificationFactory.createCombinedFilter(any())).thenReturn(new TrueSpecification());

        SearchRequest req = SearchRequest.builder().nameQuery("").searchType("name").build();
        Page<Client> page = clientSearchService.search(req, PageRequest.of(0, 10));

        assertEquals(2, page.getTotalElements());
    }

    @Test
    void shouldSearchByCompany() {
        Client c1 = new Client(); c1.setFirstName("Alice"); c1.setLastName("Smith");
        when(clientRepository.findAll()).thenReturn(List.of(c1));
        when(specificationFactory.createCompanyFilter(any())).thenReturn(c -> true);
        when(specificationFactory.createCombinedFilter(any())).thenReturn(new TrueSpecification());

        SearchRequest req = SearchRequest.builder().companyQuery("Acme").searchType("company").build();
        Page<Client> page = clientSearchService.search(req, PageRequest.of(0, 10));

        assertEquals(1, page.getTotalElements());
    }

    @Test
    void shouldPaginateResults() {
        List<Client> all = List.of(new Client(), new Client(), new Client());
        when(clientRepository.findAll()).thenReturn(all);
        when(specificationFactory.createCombinedFilter(any())).thenReturn(new TrueSpecification());

        SearchRequest req = SearchRequest.builder().build();
        Page<Client> page = clientSearchService.search(req, PageRequest.of(1, 2));

        assertEquals(3, page.getTotalElements());
        assertEquals(1, page.getContent().size());
    }
}