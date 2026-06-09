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
class ClientCompanySearchAdditionalTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private SpecificationFactory specificationFactory;

    @InjectMocks
    private ClientSearchService clientSearchService;

    @Test
    void shouldReturnAllClientsWhenCompanyQueryIsBlank() {
        when(clientRepository.findAll()).thenReturn(List.of(new Client(), new Client()));
        when(specificationFactory.createCombinedFilter(any())).thenReturn(new TrueSpecification());

        SearchRequest request = SearchRequest.builder()
                .companyQuery("")
                .searchType("company")
                .build();

        Page<Client> result = clientSearchService.search(request, PageRequest.of(0, 10));

        assertEquals(2, result.getTotalElements());
    }
}