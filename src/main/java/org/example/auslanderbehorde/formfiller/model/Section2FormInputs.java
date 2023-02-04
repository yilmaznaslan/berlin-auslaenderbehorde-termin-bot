package org.example.auslanderbehorde.formfiller.model;

import org.example.auslanderbehorde.formfiller.enums.ServiceType;
import org.example.auslanderbehorde.formfiller.intercaces.Visa;

public class Section2FormInputs {

    private final String citizenshipValue;
    private final String applicationsNumber;
    private final String familyStatus;
    private final ServiceType serviceType;
    private final Visa visa;

    public Section2FormInputs(String citizenshipValue, String applicationsNumber, String familyStatus, ServiceType serviceType, Visa visa) {
        this.citizenshipValue = citizenshipValue;
        this.applicationsNumber = applicationsNumber;
        this.familyStatus = familyStatus;
        this.serviceType = serviceType;
        this.visa = visa;
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

    public Visa getVisaEnum() {
        return visa;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }
}
