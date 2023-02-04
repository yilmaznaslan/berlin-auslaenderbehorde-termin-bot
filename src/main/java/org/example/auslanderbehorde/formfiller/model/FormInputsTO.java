package org.example.auslanderbehorde.formfiller.model;

import org.example.auslanderbehorde.formfiller.enums.EconomicActivityVisaDe;

public record FormInputsTO(String citizenshipValue,
                           String applicationsNumber,
                           String familyStatus,
                           EconomicActivityVisaDe visaEnum) {
}
