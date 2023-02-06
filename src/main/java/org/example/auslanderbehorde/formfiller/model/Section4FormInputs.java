package org.example.auslanderbehorde.formfiller.model;

import org.example.auslanderbehorde.formfiller.enums.ServiceType;

import java.util.Objects;
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

    @Override
    public String toString() {
        return "Section4FormInputs{" +
                "name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", birthdate='" + birthdate + '\'' +
                ", isResidencePermitPresent=" + isResidencePermitPresent +
                ", residencePermitId=" + residencePermitId +
                ", serviceType=" + serviceType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section4FormInputs that = (Section4FormInputs) o;
        return Objects.equals(name, that.name) && Objects.equals(lastname, that.lastname) && Objects.equals(emailAddress, that.emailAddress) && Objects.equals(birthdate, that.birthdate) && Objects.equals(isResidencePermitPresent, that.isResidencePermitPresent) && Objects.equals(residencePermitId, that.residencePermitId) && serviceType == that.serviceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, lastname, emailAddress, birthdate, isResidencePermitPresent, residencePermitId, serviceType);
    }
}

