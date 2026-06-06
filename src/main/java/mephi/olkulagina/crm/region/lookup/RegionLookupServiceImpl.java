package mephi.olkulagina.crm.region.lookup;

import mephi.olkulagina.crm.region.Region;
import mephi.olkulagina.crm.region.RegionRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionLookupServiceImpl implements RegionLookupService {
    
    private final RegionRepository regionRepository;
    
    public RegionLookupServiceImpl(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }
    
    @Override
    @Cacheable(value = "regions", unless = "#result.isEmpty()")
    public List<Region> findAll() {
        return regionRepository.findAll();
    }
    
    @Override
    @Cacheable(value = "regionSearch", key = "#query.toLowerCase()", unless = "#result.isEmpty()")
    public List<Region> findMatching(String query) {
        if (query == null || query.isBlank()) {
            return List.of();
        }
        return regionRepository.findByNameContainingIgnoreCase(query.trim());
    }
}