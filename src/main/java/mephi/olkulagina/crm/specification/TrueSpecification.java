package mephi.olkulagina.crm.specification;

import mephi.olkulagina.crm.client.Client;

public class TrueSpecification implements ClientSpecification {
    
    @Override
    public boolean isSatisfiedBy(Client client) {
        return true;
    }
}