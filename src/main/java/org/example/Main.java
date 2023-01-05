package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.auslanderbehorde.formfiller.business.FormFillerBAL;
import org.example.auslanderbehorde.formfiller.enums.EconomicActivityVisaDeEnum;
import org.example.auslanderbehorde.formfiller.model.FormInputs;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.ArrayList;
import java.util.List;

import static org.example.notifications.Helper.initDriverHeadless;

public class Main {

    public static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {


        //List<FormInputs> formInputs = generateForms();

/*
        for (FormInputs formInput: formInputs) {
            SessionFinder sessionFinder = new SessionFinder();
            sessionFinder.findRequestId();
            String requestId = sessionFinder.getRequestId();
            String dswid = sessionFinder.getDswid();
            String dsrid = sessionFinder.getDsrid();
            FormFiller formFiller = new FormFiller(requestId, dswid, dsrid, formInput, new ChromeDriver(options));
            formFiller.startScanning();
        }
*/
        RemoteWebDriver remoteWebDriver = initDriverHeadless();
        //SessionFinder sessionFinder = new SessionFinder(remoteWebDriver);
        //SessionInfo sessionInfo = sessionFinder.findAndGetSession();
        //Thread.sleep(40000);

        FormFillerBAL formFillerBAL = new FormFillerBAL(new FormInputs("163", "1", "2", EconomicActivityVisaDeEnum.BLUECARD, "name", "lastname", "yilmazn.aslan@gmail.com"), null , remoteWebDriver);
                formFillerBAL.startScanning();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutdown hook ran!");
            formFillerBAL.cancel();
            formFillerBAL.getDriver().quit();
            logger.info("quitted the driver");
            remoteWebDriver.quit();
        }
        ));

        ThreadMonitor threadMonitor = new ThreadMonitor();
        threadMonitor.startMonitoring();

        while (true) {
            Thread.sleep(1000);
        }
    }

    public static List<FormInputs> generateForms() {
        List<FormInputs> result = new ArrayList<>();
        for (EconomicActivityVisaDeEnum ecoVisa : EconomicActivityVisaDeEnum.values()) {
            result.add(new FormInputs("163", "1", "2", ecoVisa, "name", "lastname", "yilmazn.aslan@gmail.com"));
        }
        return result;
    }


    private static void cleanDrivers() {

    }
}