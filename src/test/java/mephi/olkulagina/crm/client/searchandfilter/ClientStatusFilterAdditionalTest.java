package mephi.olkulagina.crm.client.searchandfilter;

import mephi.olkulagina.crm.client.Client;
import mephi.olkulagina.crm.client.ClientRepository;
import mephi.olkulagina.crm.status.Status;
import mephi.olkulagina.crm.status.StatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ClientStatusFilterAdditionalTest {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private StatusRepository statusRepository;

    private Status statusNew;

    @BeforeEach
    void setUp() {
        statusNew = new Status(null, "New", "gray");
        statusNew = statusRepository.save(statusNew);
    }

    @Test
    void shouldFilterClientsBySingleStatus() {
        Client client = new Client();
        client.setFirstName("Alice");
        client.setLastName("Smith");
        client.setStatus(statusNew);
        clientRepository.save(client);

        List<Client> result = clientRepository.findByStatusIdIn(List.of(statusNew.getId()));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFirstName()).isEqualTo("Alice");
        assertThat(result.get(0).getStatus().getName()).isEqualTo("New");
    }
}