package mephi.olkulagina.crm.client;

public enum ClientLevel {
    BRONZE("Bronze"),
    SILVER("Silver"),
    GOLD("Gold"),
    PLATINUM("Platinum"),
    VIP("Vip");

    private final String displayName;

    ClientLevel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}