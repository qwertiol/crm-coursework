package mephi.olkulagina.crm.client;

public enum Gender {
    MALE("Male"),
    FEMALE("Female"),
    NOT_SPECIFIED("Not specified");

    private final String displayName;

    Gender(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}