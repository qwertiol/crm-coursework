package mephi.olkulagina.crm.search.specification;

import mephi.olkulagina.crm.client.Client;

import java.util.List;
import java.util.Objects;

public class CompanySpecification implements ClientSpecification {
    
    private final List<Long> companyIds;
    
    public CompanySpecification(List<Long> companyIds) {
        this.companyIds = Objects.requireNonNull(companyIds, "companyIds must not be null");
    }
    
    @Override
    public boolean isSatisfiedBy(Client client) {
        if (client.getCompany() == null) {
            return false;
        }
        return companyIds.contains(client.getCompany().getId());
    }
}