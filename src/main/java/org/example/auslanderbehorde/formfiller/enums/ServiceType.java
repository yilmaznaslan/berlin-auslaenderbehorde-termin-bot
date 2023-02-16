package org.example.auslanderbehorde.formfiller.enums;

public enum ServiceType {

    APPLY_FOR_A_RESIDENCE_TITLE("SERVICEWAHL_DE3163-0-1","163-0-1", "//*[@id=\"xi-div-30\"]/div[1]/label"),
    EXTEND_A_RESIDENCE_TITLE("SERVICEWAHL_DE3163-0-2","163-0-2", "//*[@id=\"xi-div-30\"]/div[2]/label");

    private final String id;
    private final String value;
    private final String xpath;

    ServiceType(String id, String value, String xpath) {
        this.id = id;
        this.value = value;
        this.xpath = xpath;
    }

    public String getId() {
        return id;
    }

    public String getXpath() {
        return xpath;
    }
}
