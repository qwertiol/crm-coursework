package mephi.olkulagina.crm.client.searchandfilter;

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
import mephi.olkulagina.crm.client.Client;
import mephi.olkulagina.crm.client.ClientRepository;
import mephi.olkulagina.crm.client.ClientSearchService;

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
    void shouldReturnEmptyWhenNameNotFound() {
        when(clientRepository.findByFirstNameContainingIgnoreCase("unknown")).thenReturn(List.of());
        when(clientRepository.findByLastNameContainingIgnoreCase("unknown")).thenReturn(List.of());
        when(specificationFactory.createCombinedFilter(any())).thenReturn(new TrueSpecification());

        SearchRequest request = SearchRequest.builder()
                .nameQuery("unknown")
                .searchType("name")
                .build();

        Page<Client> result = clientSearchService.search(request, PageRequest.of(0, 10));

        assertEquals(0, result.getTotalElements());
    }
}