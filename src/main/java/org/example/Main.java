package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.auslanderbehorde.formfiller.business.FormFillerBAL;
import org.example.auslanderbehorde.formfiller.model.FormInputs;
import org.example.auslanderbehorde.formfiller.enums.EconomicActivityVisaDeEnum;
import org.example.auslanderbehorde.sessionfinder.business.SessionFinder;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {

        logger.info("Initializing driver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-logging");
        options.addArguments("--headless");

        List<FormInputs> formInputs = generateForms();

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


        SessionFinder sessionFinder = new SessionFinder();
        sessionFinder.findRequestId();
        FormFillerBAL formFillerBAL = new FormFillerBAL(sessionFinder.getSessionModel(), new FormInputs("163", "1", "2", EconomicActivityVisaDeEnum.BLUECARD), new ChromeDriver(options));
        formFillerBAL.startScanning();


        ThreadMonitor threadMonitor = new ThreadMonitor();
        threadMonitor.run();
    }

    public static List<FormInputs> generateForms(){
        List<FormInputs> result = new ArrayList<>();
        for (EconomicActivityVisaDeEnum ecoVisa: EconomicActivityVisaDeEnum.values()) {
                result.add(new FormInputs("163", "1", "2", ecoVisa));
        }
        return result;
    }

}