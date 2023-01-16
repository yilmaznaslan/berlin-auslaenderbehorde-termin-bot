package org.example.auslanderbehorde.formfiller.enums;

public enum Section5FormParameterEnum {

    SEND_FORM_BUTTON("summaryForm:proceed","summaryForm:proceed");
    private final String id;
    private final String name;

    Section5FormParameterEnum(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    }
