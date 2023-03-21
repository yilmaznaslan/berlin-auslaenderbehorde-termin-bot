package org.example.model;

import java.util.Objects;

public class FormTO{

    private PersonalInfoFormTO personalInfoFormTO;
    private VisaFormTO visaFormTO;

    public FormTO() {
    }

    public FormTO(PersonalInfoFormTO personalInfoFormTO, VisaFormTO visaFormTO) {
        this.personalInfoFormTO = personalInfoFormTO;
        this.visaFormTO = visaFormTO;
    }

    public PersonalInfoFormTO getPersonalInfoFormTO() {
        return personalInfoFormTO;
    }

    public VisaFormTO getVisaFormTO() {
        return visaFormTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FormTO formTO = (FormTO) o;
        return Objects.equals(personalInfoFormTO, formTO.personalInfoFormTO) && Objects.equals(visaFormTO, formTO.visaFormTO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personalInfoFormTO, visaFormTO);
    }

    @Override
    public String toString() {
        return "FormTO{" +
                "personalInfoFormTO=" + personalInfoFormTO +
                ", visaFormTO=" + visaFormTO +
                '}';
    }
}
