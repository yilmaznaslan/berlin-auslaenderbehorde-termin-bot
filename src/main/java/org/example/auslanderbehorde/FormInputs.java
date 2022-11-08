package org.example.auslanderbehorde;

public class FormInputs {

    private final String citizenshipValue;
    private final String applicationsNumber;
    private final String familyStatus;
    private final String visaType;

    public FormInputs(String citizenshipValue, String applicationsNumber, String familyStatus, String visaType) {
        this.citizenshipValue = citizenshipValue;
        this.applicationsNumber = applicationsNumber;
        this.familyStatus = familyStatus;
        this.visaType = visaType;
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

    public String getVisaType() {
        return visaType;
    }
}
