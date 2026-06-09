package mephi.olkulagina.crm.region.lookup;

import mephi.olkulagina.crm.region.Region;

import java.util.List;

public interface RegionLookupService {
    List<Region> findAll();
    List<Region> findMatching(String query);
}