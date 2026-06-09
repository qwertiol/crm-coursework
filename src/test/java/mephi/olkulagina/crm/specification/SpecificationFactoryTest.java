package mephi.olkulagina.crm.specification;

import mephi.olkulagina.crm.company.Company;
import mephi.olkulagina.crm.company.CompanyRepository;
import mephi.olkulagina.crm.search.SearchRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpecificationFactoryTest {

    @Mock private CompanyRepository companyRepository;

    @InjectMocks
    private SpecificationFactory specificationFactory;

    @Test
    void shouldReturnTrueSpecificationWhenNoStatusIds() {
        SearchRequest req = SearchRequest.builder().build();
        ClientSpecification spec = specificationFactory.createStatusFilter(req);
        assertTrue(spec instanceof TrueSpecification);
    }

    @Test
    void shouldReturnStatusSpecificationWhenStatusIdsProvided() {
        SearchRequest req = SearchRequest.builder().statusIds(List.of(1L, 2L)).build();
        ClientSpecification spec = specificationFactory.createStatusFilter(req);
        assertTrue(spec instanceof StatusSpecification);
    }

    @Test
    void shouldReturnTrueSpecificationWhenNameQueryIsBlank() {
        SearchRequest req = SearchRequest.builder().build();
        ClientSpecification spec = specificationFactory.createNameFilter(req);
        assertTrue(spec instanceof TrueSpecification);
    }

    @Test
    void shouldReturnNameSpecificationWhenNameQueryProvided() {
        SearchRequest req = SearchRequest.builder().nameQuery("John").build();
        ClientSpecification spec = specificationFactory.createNameFilter(req);
        assertTrue(spec instanceof NameSpecification);
    }

    @Test
    void shouldReturnTrueSpecificationWhenCompanyQueryIsBlank() {
        SearchRequest req = SearchRequest.builder().build();
        ClientSpecification spec = specificationFactory.createCompanyFilter(req);
        assertTrue(spec instanceof TrueSpecification);
    }

    @Test
    void shouldReturnFalseSpecificationWhenNoCompaniesMatch() {
        when(companyRepository.findByNameContainingIgnoreCase("Unknown")).thenReturn(List.of());
        SearchRequest req = SearchRequest.builder().companyQuery("Unknown").build();
        ClientSpecification spec = specificationFactory.createCompanyFilter(req);
        assertFalse(spec.isSatisfiedBy(new mephi.olkulagina.crm.client.Client()));
    }

    @Test
    void shouldReturnCompanySpecificationWhenCompaniesMatch() {
        when(companyRepository.findByNameContainingIgnoreCase("Acme"))
            .thenReturn(List.of(new Company(1L, "Acme")));
        SearchRequest req = SearchRequest.builder().companyQuery("Acme").build();
        ClientSpecification spec = specificationFactory.createCompanyFilter(req);
        assertTrue(spec instanceof CompanySpecification);
    }

    @Test
    void combinedFilterShouldIncludeStatus() {
        SearchRequest req = SearchRequest.builder().statusIds(List.of(1L)).build();
        ClientSpecification spec = specificationFactory.createCombinedFilter(req);
        assertNotNull(spec);
        assertFalse(spec instanceof TrueSpecification);
    }
}