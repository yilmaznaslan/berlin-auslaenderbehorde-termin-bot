package org.example.auslanderbehorde.formfiller.model;

public record PersonalInfoDTO(String citizenshipValue,
                              String applicationsNumber,
                              String familyStatus,
                              String firstName,
                              String lastName,
                              String emailAddress,
                              String birthdate) {
}
