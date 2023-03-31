package org.example.model;

public class PersonalInfoFormTO {
    private String citizenshipValue;
    private String citizenshipValueOfFamilyMember;
    private String applicationsNumber;
    private String familyStatus;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String birthdate;

    public PersonalInfoFormTO() {
    }

    public PersonalInfoFormTO(String citizenshipValue, String citizenshipValueOfFamilyMember, String applicationsNumber, String familyStatus, String firstName, String lastName, String emailAddress, String birthdate) {
        this.citizenshipValue = citizenshipValue;
        this.citizenshipValueOfFamilyMember = citizenshipValueOfFamilyMember;
        this.applicationsNumber = applicationsNumber;
        this.familyStatus = familyStatus;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.birthdate = birthdate;
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

    public void setApplicationsNumber(String applicationsNumber) {
        this.applicationsNumber = applicationsNumber;
    }

    public void setFamilyStatus(String familyStatus) {
        this.familyStatus = familyStatus;
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
                ", applicationsNumber='" + applicationsNumber + '\'' +
                ", familyStatus='" + familyStatus + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", birthdate='" + birthdate + '\'' +
                '}';
    }
}
