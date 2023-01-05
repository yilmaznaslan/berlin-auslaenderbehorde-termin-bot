package org.example.auslanderbehorde.formfiller.model;

import org.example.auslanderbehorde.formfiller.enums.VisaEnum;

public class FormInputs {

    private final String citizenshipValue;
    private final String applicationsNumber;
    private final String familyStatus;
    private final VisaEnum visaEnum;
    private final String name;
    private final String lastname;
    private final String emailAddress;

    public FormInputs(String citizenshipValue, String applicationsNumber, String familyStatus, VisaEnum visaEnum, String name, String lastname, String emailAddress) {
        this.citizenshipValue = citizenshipValue;
        this.applicationsNumber = applicationsNumber;
        this.familyStatus = familyStatus;
        this.visaEnum = visaEnum;
        this.name = name;
        this.lastname = lastname;
        this.emailAddress = emailAddress;
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

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmailAddress() {
        return emailAddress;
    }
}
