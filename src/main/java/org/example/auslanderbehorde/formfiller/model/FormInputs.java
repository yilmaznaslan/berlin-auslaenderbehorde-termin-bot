package org.example.auslanderbehorde.formfiller.model;

import org.example.auslanderbehorde.formfiller.enums.VisaEnum;

public class FormInputs {

    private final String citizenshipValue;
    private final String applicationsNumber;
    private final String familyStatus;
    private final VisaEnum visaEnum;

    public FormInputs(String citizenshipValue, String applicationsNumber, String familyStatus, VisaEnum visaEnum) {
        this.citizenshipValue = citizenshipValue;
        this.applicationsNumber = applicationsNumber;
        this.familyStatus = familyStatus;
        this.visaEnum = visaEnum;
    }

    public String getCitizenshipValue() {
        return citizenshipValue;
    }

    public String getApplicationsNumber() {
        return applicationsNumber;
    }

    public String getFamilyStatus() {
        return familyStatus;
    }

    public VisaEnum getVisaEnum() {
        return visaEnum;
    }
}
