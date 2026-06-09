package mephi.olkulagina.crm.client;

import mephi.olkulagina.crm.company.Company;
import mephi.olkulagina.crm.company.CompanyRepository;
import mephi.olkulagina.crm.region.Region;
import mephi.olkulagina.crm.region.RegionRepository;
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
class ClientRepositoryIT {

    @Autowired private ClientRepository clientRepository;
    @Autowired private StatusRepository statusRepository;
    @Autowired private CompanyRepository companyRepository;
    @Autowired private RegionRepository regionRepository;

    private Status statusNew;
    private Company company;
    private Region region;

    @BeforeEach
    void setUp() {
        statusNew = new Status(null, "New", "gray");
        statusNew = statusRepository.save(statusNew);

        company = new Company(null, "Acme Corp");
        company = companyRepository.save(company);

        region = new Region(null, "London");
        region = regionRepository.save(region);
    }

    @Test
    void shouldSaveAndFindClient() {
        Client client = new Client();
        client.setFirstName("Alice");
        client.setLastName("Smith");
        client.setPhone("+15551234567");
        client.setEmail("alice@example.com");
        client.setStatus(statusNew);
        client.setCompany(company);
        client.setRegion(region);

        Client saved = clientRepository.save(client);

        assertThat(saved.getId()).isNotNull();

        Client found = clientRepository.findById(saved.getId()).orElseThrow();
        assertThat(found.getFirstName()).isEqualTo("Alice");
        assertThat(found.getLastName()).isEqualTo("Smith");
        assertThat(found.getStatus().getName()).isEqualTo("New");
    }

    @Test
    void shouldFindByLastNameContainingIgnoreCase() {
        Client client = new Client();
        client.setFirstName("Bob");
        client.setLastName("Johnson");
        client.setStatus(statusNew);
        clientRepository.save(client);

        List<Client> result = clientRepository.findByLastNameContainingIgnoreCase("john");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFirstName()).isEqualTo("Bob");
    }

    @Test
    void shouldFindByStatusIdIn() {
        Client client = new Client();
        client.setFirstName("Charlie");
        client.setLastName("Brown");
        client.setStatus(statusNew);
        clientRepository.save(client);

        List<Client> result = clientRepository.findByStatusIdIn(List.of(statusNew.getId()));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFirstName()).isEqualTo("Charlie");
    }

    @Test
    void shouldFindByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase() {
        Client client = new Client();
        client.setFirstName("David");
        client.setLastName("Miller");
        client.setStatus(statusNew);
        clientRepository.save(client);

        List<Client> result = clientRepository.findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase("dav", "mill");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFirstName()).isEqualTo("David");
    }
}