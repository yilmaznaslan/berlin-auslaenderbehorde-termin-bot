package org.example;


import org.example.errorhandling.FormValidationFailedException;
import org.example.errorhandling.FormValidator;
import org.example.forms.PersonalInfoFormTO;
import org.example.forms.VisaFormTO;
import org.example.notification.NotificationAdapter;
import org.example.notification.SoundNotifier;
import org.example.utils.DriverUtils;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.concurrent.CompletableFuture;

import static org.example.utils.IoUtils.readPersonalInfoFromFile;
import static org.example.utils.IoUtils.readVisaInfoFromFile;

public class Main {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        FormValidator formValidator = new FormValidator();
        PersonalInfoFormTO personalInfoFormTO = readPersonalInfoFromFile();
        VisaFormTO visaFormTO = readVisaInfoFromFile();

        if (formValidator.isResidenceTitleInfoVerified(visaFormTO)) {
            logger.info("Successfully validated form: {}", visaFormTO);
        } else {
            logger.error("Failed validate form: {}", visaFormTO);
            throw new FormValidationFailedException("");
        }

        NotificationAdapter notificationAdapter = new SoundNotifier();
        RemoteWebDriver webDriver = DriverUtils.initDriver();
        TerminFinder terminFinder = new TerminFinder(notificationAdapter, personalInfoFormTO, visaFormTO, webDriver);
        CompletableFuture<Boolean> scanResult = terminFinder.startScanning();

        scanResult.thenAccept(result -> logger.info("Appointments found")).exceptionally(e -> {
            logger.error("Scan failed", e);
            return null;
        });


    }

}