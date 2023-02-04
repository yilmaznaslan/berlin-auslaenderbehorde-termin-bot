package org.example.auslanderbehorde.formfiller.model;

import org.example.auslanderbehorde.formfiller.enums.ServiceType;

import java.util.Optional;

public class Section4FormInputs {

    private final String name;
    private final String lastname;
    private final String emailAddress;
    private final String birthdate;
    private final Optional<Boolean> isResidencePermitPresent;
    private final Optional<String> residencePermitId;
    private final ServiceType serviceType;

    public Section4FormInputs(String name, String lastname, String emailAddress, String birthdate, Optional<Boolean> isResidencePermitPresent, Optional<String> residencePermitId, ServiceType serviceType) {
        this.name = name;
        this.lastname = lastname;
        this.emailAddress = emailAddress;
        this.birthdate = birthdate;
        this.isResidencePermitPresent = isResidencePermitPresent;
        this.residencePermitId = residencePermitId;
        this.serviceType = serviceType;
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

    public String getBirthdate() {
        return birthdate;
    }

    public Optional<Boolean> getIsResidencePermitPresent() {
        return isResidencePermitPresent;
    }

    public Optional<String> getResidencePermitId() {
        return residencePermitId;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }
}

