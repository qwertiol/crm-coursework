package mephi.olkulagina.crm.search.specification;

import mephi.olkulagina.crm.client.Client;

import java.util.List;
import java.util.Objects;

public class StatusSpecification implements ClientSpecification {
    
    private final List<Long> statusIds;
    
    public StatusSpecification(List<Long> statusIds) {
        this.statusIds = Objects.requireNonNull(statusIds, "statusIds must not be null");
    }
    
    @Override
    public boolean isSatisfiedBy(Client client) {
        if (client.getStatus() == null) {
            return false;
        }
        return statusIds.contains(client.getStatus().getId());
    }
}