package org.example.auslanderbehorde.formfiller.model;

public class Section4FormInputs {

    private final String name;
    private final String lastname;
    private final String emailAddress;
    private final String birthdate;
    private final boolean isResidencePermitPresent;

    public Section4FormInputs(String name, String lastname, String emailAddress, String birthdate, boolean isResidencePermitPresent) {
        this.name = name;
        this.lastname = lastname;
        this.emailAddress = emailAddress;
        this.birthdate = birthdate;
        this.isResidencePermitPresent = isResidencePermitPresent;
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

    public boolean isResidencePermitPresent() {
        return isResidencePermitPresent;
    }
}
