package mephi.olkulagina.crm.region;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/regions")
public class RegionController {

    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    @GetMapping("/search")
    public List<Region> search(@RequestParam("q") String query) {
        if (query == null || query.isBlank()) {
            return List.of();
        }
        return regionService.findAll().stream()
                .filter(r -> r.getName() != null && r.getName().toLowerCase().contains(query.toLowerCase().trim()))
                .limit(10)
                .toList();
    }
}