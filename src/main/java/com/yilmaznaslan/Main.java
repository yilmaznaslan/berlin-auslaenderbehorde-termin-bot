package com.yilmaznaslan;


import com.yilmaznaslan.errorhandling.FormValidationFailedException;
import com.yilmaznaslan.errorhandling.FormValidator;
import com.yilmaznaslan.forms.VisaFormTO;
import com.yilmaznaslan.notification.AllAdapters;
import com.yilmaznaslan.notification.SlackNotifier;
import com.yilmaznaslan.notification.SoundNotifier;
import com.yilmaznaslan.notification.TwilioNotifier;
import com.yilmaznaslan.utils.DriverUtils;
import com.yilmaznaslan.utils.IoUtils;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.concurrent.CompletableFuture;

public class Main {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        FormValidator formValidator = new FormValidator();
        VisaFormTO visaFormTO = IoUtils.readVisaInfoFromFile();

        if (formValidator.isResidenceTitleInfoVerified(visaFormTO)) {
            logger.info("Successfully validated form: {}", visaFormTO);
        } else {
            logger.error("Failed validate form: {}", visaFormTO);
            throw new FormValidationFailedException("");
        }

        AllAdapters notificationAdapter = new AllAdapters(new SoundNotifier(), new TwilioNotifier(), new SlackNotifier());
        RemoteWebDriver webDriver = DriverUtils.initDriver();
        AppointmentFinder appointmentFinder = new AppointmentFinder(notificationAdapter,visaFormTO, webDriver);
        CompletableFuture<Boolean> scanResult = appointmentFinder.startScanning();

        scanResult.thenAccept(result -> logger.info("Appointments found")).exceptionally(e -> {
            logger.error("Scan failed", e);
            return null;
        });


    }

}