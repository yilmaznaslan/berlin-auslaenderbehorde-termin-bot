package org.example.auslanderbehorde.formfiller.model;

import java.util.Optional;

public class Section4FormInputs {

    private final String name;
    private final String lastname;
    private final String emailAddress;
    private final String birthdate;
    private final boolean isResidencePermitPresent;
    private final Optional<String> residencePermitId;

    public Section4FormInputs(String name, String lastname, String emailAddress, String birthdate, boolean isResidencePermitPresent, Optional<String> residencePermitId) {
        this.name = name;
        this.lastname = lastname;
        this.emailAddress = emailAddress;
        this.birthdate = birthdate;
        this.isResidencePermitPresent = isResidencePermitPresent;
        this.residencePermitId = residencePermitId;
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

    public Optional<String> getResidencePermitId() {
        return residencePermitId;
    }

    public boolean isResidencePermitPresent() {
        return isResidencePermitPresent;
    }
}
