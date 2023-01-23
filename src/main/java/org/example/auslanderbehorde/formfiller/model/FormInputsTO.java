package org.example.auslanderbehorde.formfiller.model;

import org.example.auslanderbehorde.formfiller.enums.EconomicActivityVisaDeEnum;

public record FormInputsTO(String citizenshipValue,
                           String applicationsNumber,
                           String familyStatus,
                           EconomicActivityVisaDeEnum visaEnum) {
}
