package mephi.olkulagina.crm.client;

public enum ClientSource {
    WEBSITE("Website"),
    REFERRAL("Referral"),
    SOCIAL_MEDIA("Social media"),
    DIRECT("Direct"),
    PARTNER("Partner"),
    OTHER("Other");

    private final String displayName;

    ClientSource(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}