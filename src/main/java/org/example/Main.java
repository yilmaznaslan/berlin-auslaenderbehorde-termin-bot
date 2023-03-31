package org.example;


import org.example.exceptions.FormValidationFailed;
import org.example.model.PersonalInfoFormTO;
import org.example.model.VisaFormTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.example.utils.IoUtils.readPersonalInfoFromFile;
import static org.example.utils.IoUtils.readVisaInfoFromFile;

public class Main {
    public static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws FormValidationFailed {
        PersonalInfoFormTO personalInfoFormTO = readPersonalInfoFromFile();
        VisaFormTO visaFormTO = readVisaInfoFromFile();

        TerminFinder terminFinder = new TerminFinder(personalInfoFormTO, visaFormTO);
        terminFinder.startScanning();
    }
}