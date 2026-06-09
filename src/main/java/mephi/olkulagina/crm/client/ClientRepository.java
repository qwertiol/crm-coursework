package mephi.olkulagina.crm.client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    List<Client> findByFirstNameContainingIgnoreCase(String firstName);

    List<Client> findByLastNameContainingIgnoreCase(String lastName);

    List<Client> findByStatusIdIn(List<Long> statusIds);

    List<Client> findByCompanyIdIn(List<Long> companyIds);

    List<Client> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(String firstName, String lastName);

}