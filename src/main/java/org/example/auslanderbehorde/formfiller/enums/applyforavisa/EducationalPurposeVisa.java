package org.example.auslanderbehorde.formfiller.enums.applyforavisa;

import org.example.auslanderbehorde.formfiller.intercaces.Visa;

public enum EducationalPurposeVisa implements Visa {
    LANGUAGE_COURSE ("SERVICEWAHL_EN123-0-1-3-324289", "Residence permit for attending a language course (sect. 16f para. 1)"),
    IN_SERVICE_TRAINING ("SERVICEWAHL_EN123-0-1-3-329337", "Residence permit for in-service training (sect. 16a)"),
    STUDY_PREPARATION ("SERVICEWAHL_EN123-0-1-3-305156", "Residence permit for study preparation (sect. 16b para. 1)"),
    PURPOSE_OF_STUDYING ("SERVICEWAHL_EN123-0-1-3-305244", "Residence permit for the purpose of studying (sect. 16b)"),
    VOCATIONAL_TRAINING ("SERVICEWAHL_EN123-0-1-3-328338", "Residence permit for vocational training (sect. 16a)"),
    START_TRAINEESHIP ("SERVICEWAHL_EN123-0-1-3-305303", "Residence permit to start a traineeship (sect. 19c para. 1)"),
    EXCHANGE ("SERVICEWAHL_EN123-0-1-3-326239", "Residence permit to take part in a student exchange or to attend school (sect. 16f)");

    private final String id;
    private final String dataTag0;

    EducationalPurposeVisa(String value, String dataTag0) {
        this.id = value;
        this.dataTag0 = dataTag0;
    }

    public String getId() {
        return id;
    }

    public String getDataTag0() {
        return dataTag0;
    }
}
