package mephi.olkulagina.crm.region;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    Optional<Region> findByName(String name);
    
    Optional<Region> findByNameIgnoreCase(String name);
    
    List<Region> findByNameContainingIgnoreCase(String name);
}