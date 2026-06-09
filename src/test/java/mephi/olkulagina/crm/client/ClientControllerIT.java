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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ClientControllerIT {

    @Autowired private MockMvc mockMvc;
    @Autowired private ClientRepository clientRepository;
    @Autowired private StatusRepository statusRepository;
    @Autowired private CompanyRepository companyRepository;
    @Autowired private RegionRepository regionRepository;

    private Client testClient;
    private Status statusNew;

    @BeforeEach
    void setUp() {
        statusNew = statusRepository.save(new Status(null, "New", "gray"));
        Company company = companyRepository.save(new Company(null, "Acme Corp"));
        Region region = regionRepository.save(new Region(null, "London"));

        testClient = new Client();
        testClient.setFirstName("Alice");
        testClient.setLastName("Smith");
        testClient.setPhone("+1 (555) 123-45-67");
        testClient.setStatus(statusNew);
        testClient.setCompany(company);
        testClient.setRegion(region);
        testClient = clientRepository.save(testClient);
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void shouldReturnClientListPage() throws Exception {
        mockMvc.perform(get("/clients"))
               .andExpect(status().isOk())
               .andExpect(view().name("clients"))
               .andExpect(model().attributeExists("clients", "statuses", "totalElements"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void shouldReturnClientDetailPage() throws Exception {
        mockMvc.perform(get("/clients/" + testClient.getId()))
               .andExpect(status().isOk())
               .andExpect(view().name("client-detail"))
               .andExpect(model().attributeExists("client"))
               .andExpect(model().attribute("client", org.hamcrest.Matchers.hasProperty("firstName", org.hamcrest.Matchers.is("Alice"))));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void shouldUpdateClientAndRedirect() throws Exception {
        mockMvc.perform(post("/clients/" + testClient.getId() + "/update")
                        .param("lastName", "Updated")
                        .param("firstName", "Alice")
                        .param("statusId", String.valueOf(statusNew.getId()))
                        .with(csrf()))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/clients/" + testClient.getId()));
    }

    @Test
    void shouldRedirectAnonymousToLogin() throws Exception {
        mockMvc.perform(get("/clients"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrlPattern("**/login"));
    }
}