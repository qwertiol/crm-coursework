package mephi.olkulagina.crm.specification;

import mephi.olkulagina.crm.client.Client;

@FunctionalInterface
public interface ClientSpecification {
    
    boolean isSatisfiedBy(Client client);
    
    default ClientSpecification and(ClientSpecification other) {
        return client -> this.isSatisfiedBy(client) && other.isSatisfiedBy(client);
    }
    
    default ClientSpecification or(ClientSpecification other) {
        return client -> this.isSatisfiedBy(client) || other.isSatisfiedBy(client);
    }
    
    default ClientSpecification not() {
        return client -> !this.isSatisfiedBy(client);
    }
}