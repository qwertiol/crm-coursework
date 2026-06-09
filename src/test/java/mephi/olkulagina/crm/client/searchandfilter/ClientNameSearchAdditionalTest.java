package mephi.olkulagina.crm.client.searchandfilter;

import mephi.olkulagina.crm.client.Client;
import mephi.olkulagina.crm.client.ClientRepository;
import mephi.olkulagina.crm.client.ClientSearchService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientNameSearchAdditionalTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private SpecificationFactory specificationFactory;

    @InjectMocks
    private ClientSearchService clientSearchService;

    @Test
    void shouldFindClientsByFirstName() {
        Client alice = new Client();
        alice.setFirstName("Alice");
        alice.setLastName("Smith");

        when(clientRepository.findByFirstNameContainingIgnoreCase("ali")).thenReturn(List.of(alice));
        when(clientRepository.findByLastNameContainingIgnoreCase("ali")).thenReturn(List.of());
        when(specificationFactory.createCombinedFilter(any())).thenReturn(new TrueSpecification());

        SearchRequest request = SearchRequest.builder()
                .nameQuery("ali")
                .searchType("name")
                .build();

        Page<Client> result = clientSearchService.search(request, PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals("Alice", result.getContent().get(0).getFirstName());
    }
}