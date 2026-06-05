package mephi.olkulagina.crm.company.lookup;

import mephi.olkulagina.crm.company.Company;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyLookupProxy implements CompanyLookupService {
    private final CompanyLookupServiceImpl target;
    private List<Company> cache;

    public CompanyLookupProxy(CompanyLookupServiceImpl target) {
        this.target = target;
    }

    @Override
    public List<Company> findAll() {
        if (cache == null) {
            cache = target.findAll();
        }
        return cache;
    }

    @Override
    public List<Company> findMatching(String query) {
        ensureCache();
        if (query == null || query.isBlank()) {
            return List.of();
        }
        String q = query.toLowerCase();
        return cache.stream()
                .filter(c -> c.getName() != null && c.getName().toLowerCase().contains(q))
                .limit(10)
                .collect(Collectors.toList());
    }

    public void refreshCache() {
        cache = null;
        ensureCache();
    }

    private void ensureCache() {
        if (cache == null) {
            cache = target.findAll();
        }
    }
}