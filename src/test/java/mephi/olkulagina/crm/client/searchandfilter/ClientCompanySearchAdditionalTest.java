package mephi.olkulagina.crm.client.searchandfilter;

import mephi.olkulagina.crm.client.Client;
import mephi.olkulagina.crm.client.ClientRepository;
import mephi.olkulagina.crm.client.ClientSearchService;
import mephi.olkulagina.crm.company.Company;
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
class ClientCompanySearchAdditionalTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private SpecificationFactory specificationFactory;

    @InjectMocks
    private ClientSearchService clientSearchService;

    @Test
    void shouldFindClientsByCompanyName() {
        Company acme = new Company(1L, "Acme Corp");
        
        Client client = new Client();
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setCompany(acme);

        when(clientRepository.findAll()).thenReturn(List.of(client));
        when(specificationFactory.createCompanyFilter(any())).thenReturn(c -> 
            c.getCompany() != null && c.getCompany().getName().contains("Acme"));
        when(specificationFactory.createCombinedFilter(any())).thenReturn(new TrueSpecification());

        SearchRequest request = SearchRequest.builder()
                .companyQuery("Acme")
                .searchType("company")
                .build();

        Page<Client> result = clientSearchService.search(request, PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals("Acme Corp", result.getContent().get(0).getCompany().getName());
    }
}