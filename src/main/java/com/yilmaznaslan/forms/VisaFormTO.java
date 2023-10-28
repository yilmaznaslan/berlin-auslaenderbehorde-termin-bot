package com.yilmaznaslan.forms;

import java.util.Objects;

public class VisaFormTO {

    private String citizenshipValue;
    private String numberOfApplicants;
    private String isThereFamilyMember;
    private String citizenshipValueOfFamilyMember;
    private String serviceType;
    private String visaType;
    private String visaPurpose;

    public VisaFormTO() {
    }

    public VisaFormTO(String citizenshipValue, String numberOfApplicants, String isThereFamilyMember,
                      String citizenshipValueOfFamilyMember, String serviceType, String visaType, String visaPurpose) {
        this.citizenshipValue = citizenshipValue;
        this.numberOfApplicants = numberOfApplicants;
        this.isThereFamilyMember = isThereFamilyMember;
        this.citizenshipValueOfFamilyMember = citizenshipValueOfFamilyMember;
        this.serviceType = serviceType;
        this.visaType = visaType;
        this.visaPurpose = visaPurpose;
    }

    public String getCitizenshipValue() {
        return citizenshipValue;
    }

    public String getNumberOfApplicants() {
        return numberOfApplicants;
    }

    public String getIsThereFamilyMember() {
        return isThereFamilyMember;
    }

    public String getCitizenshipValueOfFamilyMember() {
        return citizenshipValueOfFamilyMember;
    }

    public String getServiceType() {
        return serviceType;
    }

    public String getVisaType() {
        return visaType;
    }

    public String getVisaPurpose() {
        return visaPurpose;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VisaFormTO that = (VisaFormTO) o;
        return Objects.equals(citizenshipValue, that.citizenshipValue) && Objects.equals(numberOfApplicants, that.numberOfApplicants) && Objects.equals(isThereFamilyMember, that.isThereFamilyMember) && Objects.equals(citizenshipValueOfFamilyMember, that.citizenshipValueOfFamilyMember) && Objects.equals(serviceType, that.serviceType) && Objects.equals(visaType, that.visaType) && Objects.equals(visaPurpose, that.visaPurpose);
    }

    @Override
    public int hashCode() {
        return Objects.hash(citizenshipValue, numberOfApplicants, isThereFamilyMember, citizenshipValueOfFamilyMember, serviceType, visaType, visaPurpose);
    }

}

