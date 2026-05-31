package mephi.olkulagina.crm.client;

import lombok.RequiredArgsConstructor;
import mephi.olkulagina.crm.status.Status;
import mephi.olkulagina.crm.status.StatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final StatusRepository statusRepository;

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public Optional<Client> findById(Long id) {
        return clientRepository.findById(id);
    }

    public Client save(Client client) {
        return clientRepository.save(client);
    }

    public Client updateStatus(Long clientId, Long statusId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Клиент не найден с id: " + clientId));
        Status status = statusRepository.findById(statusId)
                .orElseThrow(() -> new RuntimeException("Статус не найден с id: " + statusId));
        client.setStatus(status);
        return clientRepository.save(client);
    }
}