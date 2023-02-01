package org.example.auslanderbehorde.formfiller.enums;

public enum ServiceTypeEnum {

    APPLY_FOR_A_RESIDENCE_TITLE("SERVICEWAHL_DE3163-0-1","163-0-1"),
    EXTEND_A_RESIDENCE_TITLE("",""),
    TRANSFER_OF_A_RESIDENCE_TITLE_TO_A_NEW_PASSPORT("",""),
    APPLY_FOR_A_PERMANENT_SETTLEMENT_PERMIT("",""),
    PASSPORT_SUBSTITUTE_REISSUE("","");

    private final String id;
    private final String value;

    ServiceTypeEnum(String id, String value) {
        this.id = id;
        this.value = value;
    }
}
