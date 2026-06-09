package mephi.olkulagina.crm.region;

import mephi.olkulagina.crm.region.lookup.RegionLookupService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/regions")
public class RegionController {

    private final RegionLookupService regionLookupService;

    public RegionController(RegionLookupService regionLookupService) {
        this.regionLookupService = regionLookupService;
    }

    @GetMapping("/search")
    public List<Region> search(@RequestParam("q") String query) {
        return regionLookupService.findMatching(query);
    }
}