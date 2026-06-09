package mephi.olkulagina.crm.client.searchandfilter;

import mephi.olkulagina.crm.status.Status;
import mephi.olkulagina.crm.status.StatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import mephi.olkulagina.crm.client.Client;
import mephi.olkulagina.crm.client.ClientRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ClientStatusFilterAdditionalTest {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private StatusRepository statusRepository;

    private Status statusNew;
    private Status statusDeal;

    @BeforeEach
    void setUp() {
        statusNew = new Status(null, "New", "gray");
        statusNew = statusRepository.save(statusNew);

        statusDeal = new Status(null, "Deal", "green");
        statusDeal = statusRepository.save(statusDeal);
    }

    @Test
    void shouldFindClientsByMultipleStatusIds() {
        Client client1 = new Client();
        client1.setFirstName("Alice");
        client1.setLastName("Smith");
        client1.setStatus(statusNew);
        clientRepository.save(client1);

        Client client2 = new Client();
        client2.setFirstName("Bob");
        client2.setLastName("Jones");
        client2.setStatus(statusDeal);
        clientRepository.save(client2);

        List<Client> result = clientRepository.findByStatusIdIn(
                List.of(statusNew.getId(), statusDeal.getId()));

        assertThat(result).hasSize(2);
        assertThat(result)
                .extracting(Client::getFirstName)
                .containsExactlyInAnyOrder("Alice", "Bob");
    }
}