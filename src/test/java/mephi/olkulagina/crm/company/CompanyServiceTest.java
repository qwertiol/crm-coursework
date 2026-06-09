package mephi.olkulagina.crm.company;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @Mock private CompanyRepository companyRepository;

    @InjectMocks
    private CompanyService companyService;

    @Test
    void shouldReturnNullWhenNameIsNullOrBlank() {
        assertNull(companyService.findOrCreate(null));
        assertNull(companyService.findOrCreate("   "));
    }

    @Test
    void shouldReturnExistingCompanyWhenFound() {
        Company existing = new Company(1L, "Acme");
        when(companyRepository.findByNameIgnoreCase("Acme")).thenReturn(Optional.of(existing));

        Company result = companyService.findOrCreate("Acme");

        assertEquals(existing, result);
        verify(companyRepository, never()).save(any());
    }

    @Test
    void shouldCreateNewCompanyWhenNotFound() {
        when(companyRepository.findByNameIgnoreCase("NewCorp")).thenReturn(Optional.empty());
        when(companyRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Company result = companyService.findOrCreate("NewCorp");

        assertNotNull(result);
        assertEquals("NewCorp", result.getName());
        verify(companyRepository).save(any());
    }
}