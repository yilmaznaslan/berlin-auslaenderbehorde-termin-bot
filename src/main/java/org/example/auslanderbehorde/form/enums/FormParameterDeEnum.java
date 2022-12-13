package org.example.auslanderbehorde.form.enums;

public enum FormParameterDeEnum {

    COUNTRY("xi-sel-400", "sel-staat"),
    APPLICANT_COUNT("xi-sel-422", "personenAnzahl_normal"),
    FAMILY_STATUS("xi-sel-427", "lebnBrMitFmly");

    private final String id;
    private final String name;

    FormParameterDeEnum(String id, String name) {
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
