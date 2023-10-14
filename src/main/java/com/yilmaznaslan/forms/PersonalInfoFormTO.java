package com.yilmaznaslan.forms;

public class PersonalInfoFormTO {
    private String citizenshipValue;
    private String citizenshipValueOfFamilyMember;
    private String numberOfApplicants;
    private String isThereFamilyMember;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String birthdate;

    public PersonalInfoFormTO() {
    }

    public PersonalInfoFormTO(String citizenshipValue, String citizenshipValueOfFamilyMember, String numberOfApplicants, String isThereFamilyMember, String firstName, String lastName, String emailAddress, String birthdate) {
        this.citizenshipValue = citizenshipValue;
        this.citizenshipValueOfFamilyMember = citizenshipValueOfFamilyMember;
        this.numberOfApplicants = numberOfApplicants;
        this.isThereFamilyMember = isThereFamilyMember;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.birthdate = birthdate;
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

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setCitizenshipValue(String citizenshipValue) {
        this.citizenshipValue = citizenshipValue;
    }

    public void setNumberOfApplicants(String numberOfApplicants) {
        this.numberOfApplicants = numberOfApplicants;
    }

    public void setIsThereFamilyMember(String isThereFamilyMember) {
        this.isThereFamilyMember = isThereFamilyMember;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getCitizenshipValueOfFamilyMember() {
        return citizenshipValueOfFamilyMember;
    }

    public void setCitizenshipValueOfFamilyMember(String citizenshipValueOfFamilyMember) {
        this.citizenshipValueOfFamilyMember = citizenshipValueOfFamilyMember;
    }

    @Override
    public String toString() {
        return "PersonalInfoFormTO{" +
                "citizenshipValue='" + citizenshipValue + '\'' +
                ", citizenshipValueOfFamilyMember='" + citizenshipValueOfFamilyMember + '\'' +
                ", numberOfApplicants='" + numberOfApplicants + '\'' +
                ", isThereFamilyMember='" + isThereFamilyMember + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", birthdate='" + birthdate + '\'' +
                '}';
    }
}
