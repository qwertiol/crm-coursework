package mephi.olkulagina.crm.client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    List<Client> findByFullNameContainingIgnoreCase(String name);

    List<Client> findByCompanyContainingIgnoreCase(String company);

    List<Client> findByStatusId(Long statusId);
}