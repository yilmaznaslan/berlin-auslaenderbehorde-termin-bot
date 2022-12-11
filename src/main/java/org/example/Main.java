package org.example;

import org.example.auslanderbehorde.FormFiller;
import org.example.auslanderbehorde.FormInputs;
import org.example.auslanderbehorde.SessionFinder;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    public static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {

        SessionFinder sessionFinder = new SessionFinder();
        sessionFinder.findRequestId();
        String requestId = sessionFinder.getRequestId();
        String dswid = sessionFinder.getDswid();
        String dsrid = sessionFinder.getDsrid();

        String visaTypeXPath = "//*[@id=\"inner-163-0-1\"]/div/div[4]/div/div[11]/label";
        FormInputs formInputs = new FormInputs("163", "1", "2", visaTypeXPath);
        logger.info("Initializing driver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-logging");
        options.addArguments("--headless");

        FormFiller formFiller = new FormFiller(requestId, dswid, dsrid, formInputs, new ChromeDriver(options));
        formFiller.startScanning();



        ThreadMonitor threadMonitor = new ThreadMonitor();
        threadMonitor.run();
    }

}