package mephi.olkulagina.crm.client;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    List<Client> findByStatusId(Long statusId);

    List<Client> findByStatusIdIn(List<Long> statusIds);

    Page<Client> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName, Pageable pageable);

    Page<Client> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(String firstName, String lastName, Pageable pageable);

    Page<Client> findByCompanyIdIn(List<Long> companyIds, Pageable pageable);

    List<Client> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(String firstName, String lastName);

    List<Client> findByCompanyIdIn(List<Long> companyIds);
}