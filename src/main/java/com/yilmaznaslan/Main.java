package com.yilmaznaslan;


import java.util.concurrent.CompletableFuture;

import org.openqa.selenium.remote.RemoteWebDriver;

import com.yilmaznaslan.errorhandling.FormValidationFailedException;
import com.yilmaznaslan.errorhandling.FormValidator;
import com.yilmaznaslan.forms.PersonalInfoFormTO;
import com.yilmaznaslan.forms.VisaFormTO;
import com.yilmaznaslan.notification.NotificationAdapter;
import com.yilmaznaslan.notification.SoundNotifier;
import com.yilmaznaslan.utils.DriverUtils;
import com.yilmaznaslan.utils.IoUtils;

public class Main {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        FormValidator formValidator = new FormValidator();
        PersonalInfoFormTO personalInfoFormTO = IoUtils.readPersonalInfoFromFile();
        VisaFormTO visaFormTO = IoUtils.readVisaInfoFromFile();

        if (formValidator.isResidenceTitleInfoVerified(visaFormTO)) {
            logger.info("Successfully validated form: {}", visaFormTO);
        } else {
            logger.error("Failed validate form: {}", visaFormTO);
            throw new FormValidationFailedException("");
        }

        NotificationAdapter notificationAdapter = new SoundNotifier();
        RemoteWebDriver webDriver = DriverUtils.initDriver();
        AppointmentFinder appointmentFinder = new AppointmentFinder(notificationAdapter, personalInfoFormTO, visaFormTO, webDriver);
        CompletableFuture<Boolean> scanResult = appointmentFinder.startScanning();

        scanResult.thenAccept(result -> logger.info("Appointments found")).exceptionally(e -> {
            logger.error("Scan failed", e);
            return null;
        });


    }

}