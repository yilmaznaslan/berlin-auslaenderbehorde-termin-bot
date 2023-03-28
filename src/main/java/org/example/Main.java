package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.exceptions.FormValidationFailed;
import org.example.model.PersonalInfoFormTO;
import org.example.model.VisaFormTO;

import static org.example.utils.IoUtils.readPersonalInfoFromFile;
import static org.example.utils.IoUtils.readVisaInfoFromFile;

public class Main {
    public static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws FormValidationFailed {
        PersonalInfoFormTO personalInfoFormTO = readPersonalInfoFromFile();
        VisaFormTO visaFormTO = readVisaInfoFromFile();

        TerminFinder terminFinder = new TerminFinder(personalInfoFormTO, visaFormTO);
        terminFinder.startScanning();
    }
}