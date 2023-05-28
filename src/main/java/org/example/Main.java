package org.example;


import org.example.exceptions.FormValidationFailed;
import org.example.model.PersonalInfoFormTO;
import org.example.model.VisaFormTO;

import java.io.IOException;
import java.util.UUID;

import static org.example.utils.IoUtils.readPersonalInfoFromFile;
import static org.example.utils.IoUtils.readVisaInfoFromFile;

public class Main {

    public static void main(String[] args) throws FormValidationFailed, IOException {
        PersonalInfoFormTO personalInfoFormTO = readPersonalInfoFromFile();
        VisaFormTO visaFormTO = readVisaInfoFromFile();
        UUID id = UUID.randomUUID();
        TerminFinder terminFinder = new TerminFinder(id, personalInfoFormTO, visaFormTO);
        terminFinder.startScanning();
    }
}