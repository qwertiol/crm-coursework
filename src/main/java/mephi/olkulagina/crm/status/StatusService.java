package mephi.olkulagina.crm.status;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StatusService {

    private final StatusRepository statusRepository;

    public List<Status> findAll() {
        return statusRepository.findAll();
    }

    public Optional<Status> findById(Long id) {
        return statusRepository.findById(id);
    }

    public Optional<Status> findByName(String name) {
        return statusRepository.findByName(name);
    }
}