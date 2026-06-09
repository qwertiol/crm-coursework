package mephi.olkulagina.crm.client.searchandfilter;

import mephi.olkulagina.crm.client.Client;
import mephi.olkulagina.crm.client.ClientRepository;
import mephi.olkulagina.crm.company.Company;
import mephi.olkulagina.crm.company.CompanyRepository;
import mephi.olkulagina.crm.region.Region;
import mephi.olkulagina.crm.region.RegionRepository;
import mephi.olkulagina.crm.status.Status;
import mephi.olkulagina.crm.status.StatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class SearchAndFilterIT {

    @Autowired private MockMvc mockMvc;
    @Autowired private ClientRepository clientRepository;
    @Autowired private StatusRepository statusRepository;
    @Autowired private CompanyRepository companyRepository;
    @Autowired private RegionRepository regionRepository;

    private Status statusNew;
    private Status statusDeal;
    private Company acme;
    private Company globex;

    @BeforeEach
    void setUp() {
        statusNew = statusRepository.save(new Status(null, "New", "gray"));
        statusDeal = statusRepository.save(new Status(null, "Deal", "green"));

        acme = companyRepository.save(new Company(null, "Acme Corp"));
        globex = companyRepository.save(new Company(null, "Globex"));

        Region london = regionRepository.save(new Region(null, "London"));

        Client alice = new Client();
        alice.setFirstName("Alice");
        alice.setLastName("Smith");
        alice.setStatus(statusNew);
        alice.setCompany(acme);
        alice.setRegion(london);
        clientRepository.save(alice);

        Client bob = new Client();
        bob.setFirstName("Bob");
        bob.setLastName("Jones");
        bob.setStatus(statusDeal);
        bob.setCompany(globex);
        bob.setRegion(london);
        clientRepository.save(bob);

        Client charlie = new Client();
        charlie.setFirstName("Charlie");
        charlie.setLastName("Acme");
        charlie.setStatus(statusNew);
        charlie.setCompany(acme);
        charlie.setRegion(london);
        clientRepository.save(charlie);
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void shouldFilterClientsByStatus() throws Exception {
        mockMvc.perform(get("/clients").param("statusIds", String.valueOf(statusNew.getId())))
               .andExpect(status().isOk())
               .andExpect(view().name("clients"))
               .andExpect(model().attribute("resultsCount", 2L));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void shouldSearchClientsByName() throws Exception {
        mockMvc.perform(get("/clients")
                        .param("nameQuery", "Alice")
                        .param("searchType", "name"))
               .andExpect(status().isOk())
               .andExpect(view().name("clients"))
               .andExpect(model().attribute("resultsCount", 1L));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void shouldSearchClientsByCompany() throws Exception {
        mockMvc.perform(get("/clients")
                        .param("companyQuery", "Globex")
                        .param("searchType", "company"))
               .andExpect(status().isOk())
               .andExpect(view().name("clients"))
               .andExpect(model().attribute("resultsCount", 1L));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void shouldCombineFilterAndSearch() throws Exception {
        mockMvc.perform(get("/clients")
                        .param("statusIds", String.valueOf(statusNew.getId()))
                        .param("nameQuery", "Alice")
                        .param("searchType", "name"))
               .andExpect(status().isOk())
               .andExpect(view().name("clients"))
               .andExpect(model().attribute("resultsCount", 1L));
    }
}