package mephi.olkulagina.crm.search.specification;

import mephi.olkulagina.crm.client.Client;

import java.util.Objects;

public class NameSpecification implements ClientSpecification {

    private final String query;

    public NameSpecification(String query) {
        this.query = Objects.requireNonNull(query, "query must not be null").toLowerCase().trim();
    }

    @Override
    public boolean isSatisfiedBy(Client client) {
        String firstName = client.getFirstName() != null ? client.getFirstName().toLowerCase() : "";
        String lastName = client.getLastName() != null ? client.getLastName().toLowerCase() : "";
        String fullName = (firstName + " " + lastName).trim();

        return fullName.contains(query);
    }
}