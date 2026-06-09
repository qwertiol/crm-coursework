package mephi.olkulagina.crm.client;

import mephi.olkulagina.crm.company.Company;
import mephi.olkulagina.crm.company.CompanyService;
import mephi.olkulagina.crm.region.Region;
import mephi.olkulagina.crm.region.RegionService;
import mephi.olkulagina.crm.status.Status;
import mephi.olkulagina.crm.status.StatusRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock private ClientRepository clientRepository;
    @Mock private StatusRepository statusRepository;
    @Mock private CompanyService companyService;
    @Mock private RegionService regionService;

    @InjectMocks
    private ClientService clientService;

    @Test
    void shouldThrowWhenLastNameIsEmpty() {
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
            clientService.updateClient(1L, "", "John", null, null, null,
                null, null, null, null, null, null, null, null, null, null)
        );
        assertEquals("Last name is required", ex.getMessage());
    }

    @Test
    void shouldThrowWhenFirstNameIsEmpty() {
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
            clientService.updateClient(1L, "Doe", "", null, null, null,
                null, null, null, null, null, null, null, null, null, null)
        );
        assertEquals("First name is required", ex.getMessage());
    }

    @Test
    void shouldThrowWhenEmailIsInvalid() {
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
            clientService.updateClient(1L, "Doe", "John", null, null, "not-an-email",
                null, null, null, null, null, null, null, null, null, null)
        );
        assertTrue(ex.getMessage().toLowerCase().contains("email"));
    }

    @Test
    void shouldThrowWhenPhoneIsInvalid() {
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
            clientService.updateClient(1L, "Doe", "John", null, "abc", null,
                null, null, null, null, null, null, null, null, null, null)
        );
        assertTrue(ex.getMessage().toLowerCase().contains("phone"));
    }

    @Test
    void shouldThrowWhenStatusNotFound() {
        when(statusRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
            clientService.updateClient(1L, "Doe", "John", null, null, null,
                null, null, null, null, null, null, null, null, null, 99L)
        );
        assertEquals("Status not found", ex.getMessage());
    }

    @Test
    void shouldUpdateClientSuccessfully() {
        Client client = new Client();
        client.setId(1L);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(companyService.findOrCreate("Acme")).thenReturn(new Company(1L, "Acme"));
        when(regionService.findOrCreate("NY")).thenReturn(new Region(1L, "NY"));
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        Client result = clientService.updateClient(1L, "Doe", "John", null, null, null,
            null, null, null, null, "Acme", "NY", null, null, null, null);

        assertEquals("Doe", result.getLastName());
        assertEquals("John", result.getFirstName());
        verify(clientRepository).save(client);
    }

    @Test
    void shouldQuickChangeStatus() {
        Client client = new Client();
        client.setId(1L);
        Status status = new Status(2L, "Deal", "green");
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(statusRepository.findById(2L)).thenReturn(Optional.of(status));
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        Client result = clientService.quickStatusChange(1L, 2L);

        assertEquals(status, result.getStatus());
        verify(clientRepository).save(client);
    }
}