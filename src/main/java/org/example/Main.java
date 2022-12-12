package org.example;

import org.example.auslanderbehorde.form.FormFiller;
import org.example.auslanderbehorde.form.FormInputs;
import org.example.auslanderbehorde.SessionFinder;
import org.example.auslanderbehorde.form.enums.EconomicActivityVisaDeEnum;
import org.example.auslanderbehorde.form.enums.EconomicActivityVisaEnEnum;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {

        logger.info("Initializing driver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-logging");
        options.addArguments("--headless");

        List<FormInputs> formInputs = generateForms();

        for (FormInputs formInput: formInputs) {
            SessionFinder sessionFinder = new SessionFinder();
            sessionFinder.findRequestId();
            String requestId = sessionFinder.getRequestId();
            String dswid = sessionFinder.getDswid();
            String dsrid = sessionFinder.getDsrid();
            FormFiller formFiller = new FormFiller(requestId, dswid, dsrid, formInput, new ChromeDriver(options));
            formFiller.startScanning();
        }


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