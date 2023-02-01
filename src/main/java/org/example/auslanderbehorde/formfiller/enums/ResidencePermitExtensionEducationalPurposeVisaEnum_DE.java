package org.example.auslanderbehorde.formfiller.enums;

public enum ResidencePermitExtensionEducationalPurposeVisaEnum_DE implements VisaEnum  {

    PURPOSE_OF_STUDYING ("SERVICEWAHL_DE163-0-2-3-305244", "Aufenthaltserlaubnis zum Studium (§ 16b)");


    private final String id;
    private final String dataTag0;

    ResidencePermitExtensionEducationalPurposeVisaEnum_DE(String id, String dataTag0) {
        this.id = id;
        this.dataTag0 = dataTag0;
    }

    public String getId() {
        return id;
    }

    public String getDataTag0() {
        return dataTag0;
    }
}
