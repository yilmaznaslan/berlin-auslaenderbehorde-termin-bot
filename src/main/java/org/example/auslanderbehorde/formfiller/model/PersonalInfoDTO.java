package org.example.auslanderbehorde.formfiller.model;

public class PersonalInfoDTO {

    private final String citizenshipValue;
    private final String applicationsNumber;
    private final String familyStatus;
    private final String firstName;
    private final String lastname;
    private final String emailAddress;
    private final String birthdate;

    public PersonalInfoDTO(String citizenshipValue, String applicationsNumber, String familyStatus, String name, String lastname, String emailAddress, String birthdate) {
        this.citizenshipValue = citizenshipValue;
        this.applicationsNumber = applicationsNumber;
        this.familyStatus = familyStatus;
        this.firstName = name;
        this.lastname = lastname;
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

    public String getLastname() {
        return lastname;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getBirthdate() {
        return birthdate;
    }
}
