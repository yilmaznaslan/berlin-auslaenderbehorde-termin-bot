package org.example;


import org.example.exceptions.FormValidationFailed;
import org.example.model.PersonalInfoFormTO;
import org.example.model.VisaFormTO;

import static org.example.utils.IoUtils.readPersonalInfoFromFile;
import static org.example.utils.IoUtils.readVisaInfoFromFile;

public class Main {

    public static void main(String[] args) throws FormValidationFailed {
        PersonalInfoFormTO personalInfoFormTO = readPersonalInfoFromFile();
        VisaFormTO visaFormTO = readVisaInfoFromFile();

        TerminFinder terminFinder = new TerminFinder(personalInfoFormTO, visaFormTO);
        terminFinder.startScanning();
    }
}