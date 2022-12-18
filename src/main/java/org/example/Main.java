package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.example.auslanderbehorde.form.business.FormFiller;
import org.example.auslanderbehorde.form.FormInputs;
import org.example.auslanderbehorde.SessionFinder;
import org.example.auslanderbehorde.form.enums.EconomicActivityVisaDeEnum;
import org.example.auslanderbehorde.form.enums.VisaEnum;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

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
        String requestId = sessionFinder.getRequestId();
        String dswid = sessionFinder.getDswid();
        String dsrid = sessionFinder.getDsrid();
        Proxy p=new Proxy();


        p.setHttpProxy("www.abc.com:8080");
        options.setCapability(CapabilityType.PROXY, p);


        FormFiller formFiller = new FormFiller(requestId, dswid, dsrid, new FormInputs("163", "1", "2", EconomicActivityVisaDeEnum.BLUECARD), new ChromeDriver(options));
        formFiller.startScanning();




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