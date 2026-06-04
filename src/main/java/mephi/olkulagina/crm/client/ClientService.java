package mephi.olkulagina.crm.client;

import mephi.olkulagina.crm.status.Status;
import mephi.olkulagina.crm.status.StatusRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClientService {

    private final ClientRepository clientRepository;
    private final StatusRepository statusRepository;

    public ClientService(ClientRepository clientRepository, StatusRepository statusRepository) {
        this.clientRepository = clientRepository;
        this.statusRepository = statusRepository;
    }

    @Transactional(readOnly = true)
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Client> findById(Long id) {
        return clientRepository.findById(id);
    }

    public Client save(Client client) {
        return clientRepository.save(client);
    }

    public Client updateStatus(Long clientId, Long statusId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + clientId));
        Status status = statusRepository.findById(statusId)
                .orElseThrow(() -> new RuntimeException("Status not found with id: " + statusId));
        client.setStatus(status);
        return clientRepository.save(client);
    }

    @Transactional(readOnly = true)
    public List<Client> findByLastNameContainingIgnoreCase(String lastName) {
        return clientRepository.findByLastNameContainingIgnoreCase(lastName);
    }

    @Transactional(readOnly = true)
    public List<Client> findByFirstNameContainingIgnoreCase(String firstName) {
        return clientRepository.findByFirstNameContainingIgnoreCase(firstName);
    }

    @Transactional(readOnly = true)
    public List<Client> findByEmailContainingIgnoreCase(String email) {
        return clientRepository.findByEmailContainingIgnoreCase(email);
    }

    @Transactional(readOnly = true)
    public List<Client> findByPhoneContaining(String phone) {
        return clientRepository.findByPhoneContaining(phone);
    }

    @Transactional(readOnly = true)
    public List<Client> findByRegionId(Long regionId) {
        return clientRepository.findByRegionId(regionId);
    }

    @Transactional(readOnly = true)
    public List<Client> findByCompanyId(Long companyId) {
        return clientRepository.findByCompanyId(companyId);
    }

    @Transactional(readOnly = true)
    public List<Client> findByGender(Gender gender) {
        return clientRepository.findByGender(gender);
    }

    @Transactional(readOnly = true)
    public List<Client> findBySource(ClientSource source) {
        return clientRepository.findBySource(source);
    }

    @Transactional(readOnly = true)
    public List<Client> findByStatusId(Long statusId) {
        return clientRepository.findByStatusId(statusId);
    }
}