package mephi.olkulagina.crm.client.validation;

import mephi.olkulagina.crm.client.ClientRepository;
import mephi.olkulagina.crm.client.ClientService;
import mephi.olkulagina.crm.company.CompanyService;
import mephi.olkulagina.crm.region.RegionService;
import mephi.olkulagina.crm.status.StatusRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ValidationStrategyTest {

    @Mock private ClientRepository clientRepository;
    @Mock private StatusRepository statusRepository;
    @Mock private CompanyService companyService;
    @Mock private RegionService regionService;

    @InjectMocks
    private ClientService clientService;

    @Test
    void shouldAcceptValidEmail() {
        List<String> errors = clientService.validateClientData("user@example.com", null);
        assertTrue(errors.isEmpty());
    }

    @Test
    void shouldRejectInvalidEmail() {
        List<String> errors = clientService.validateClientData("invalid-email", null);
        assertFalse(errors.isEmpty());
        assertTrue(errors.get(0).toLowerCase().contains("email"));
    }

    @Test
    void shouldAcceptValidPhone() {
        List<String> errors = clientService.validateClientData(null, "+1 (780) 180-20-72");
        assertTrue(errors.isEmpty());
    }

    @Test
    void shouldRejectInvalidPhone() {
        List<String> errors = clientService.validateClientData(null, "not-a-phone");
        assertFalse(errors.isEmpty());
        assertTrue(errors.get(0).toLowerCase().contains("phone"));
    }

    @Test
    void shouldAcceptNullValues() {
        List<String> errors = clientService.validateClientData(null, null);
        assertTrue(errors.isEmpty());
    }
}