package org.example.auslanderbehorde.formfiller.enums.visaextension;

import org.example.auslanderbehorde.formfiller.intercaces.VisaExtensionForEducationPurpose;

public enum VisaExtensionForEducationalPurposeVisaEnum_DE implements VisaExtensionForEducationPurpose {

    PURPOSE_OF_STUDYING ("SERVICEWAHL_DE163-0-2-3-305244", "Aufenthaltserlaubnis zum Studium (§ 16b)");

    private final String id;
    private final String dataTag0;

    VisaExtensionForEducationalPurposeVisaEnum_DE(String id, String dataTag0) {
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
