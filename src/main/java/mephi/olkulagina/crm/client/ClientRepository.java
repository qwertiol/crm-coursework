package mephi.olkulagina.crm.client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    List<Client> findByLastNameContainingIgnoreCase(String lastName);

    List<Client> findByFirstNameContainingIgnoreCase(String firstName);

    List<Client> findByEmailContainingIgnoreCase(String email);

    List<Client> findByPhoneContaining(String phone);

    List<Client> findByRegionId(Long regionId);

    List<Client> findByCompanyId(Long companyId);

    List<Client> findByGender(Gender gender);

    List<Client> findBySource(ClientSource source);

    List<Client> findByStatusId(Long statusId);
}