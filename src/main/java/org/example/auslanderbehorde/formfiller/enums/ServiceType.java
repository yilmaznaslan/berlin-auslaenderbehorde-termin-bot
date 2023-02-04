package org.example.auslanderbehorde.formfiller.enums;

public enum ServiceType {

    APPLY_FOR_A_RESIDENCE_TITLE("SERVICEWAHL_DE3163-0-1","163-0-1"),
    EXTEND_A_RESIDENCE_TITLE("SERVICEWAHL_DE3163-0-2","163-0-2");

    private final String id;
    private final String value;

    ServiceType(String id, String value) {
        this.id = id;
        this.value = value;
    }
}
