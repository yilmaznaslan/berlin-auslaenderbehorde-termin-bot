package org.example.enums;

public enum Section4FormParameterEnum {

    FIRSTNAME("xi-tf-3","antragsteller_vname"),
    LASTNAME("xi-tf-4", "antragsteller_nname"),
    BIRTHDATE("xi-tf-5", "antragsteller_gebDatum"),
    EMAIL("xi-tf-6", "antragsteller_email" ),
    RESIDENCE_PERMIT("xi-sel-2", "sel_aufenthaltserlaubnis"),
    RESIDENCE_PERMIT_NUMBER("xi-tf-7", "antragsteller_nrAufenthaltserlaubnis"),

    RESIDENCE_PERMIT_NUMBER_EXTENSION("xi-tf-21","antragsteller_nrAufenthaltserlaubnis2" );
    private final String id;
    private final String name;

    Section4FormParameterEnum(String id, String name) {
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
