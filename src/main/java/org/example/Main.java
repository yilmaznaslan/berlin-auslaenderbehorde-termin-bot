package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.auslanderbehorde.formfiller.business.TerminFinder;
import org.example.auslanderbehorde.formfiller.enums.EconomicActivityVisaDe;
import org.example.auslanderbehorde.formfiller.enums.VisaExtensionForEducationalPurposeVisaEnum_DE;
import org.example.auslanderbehorde.formfiller.enums.ServiceType;
import org.example.auslanderbehorde.formfiller.model.Section2FormInputs;
import org.example.auslanderbehorde.formfiller.model.Section4FormInputs;
import org.openqa.selenium.remote.RemoteWebDriver;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.example.auslanderbehorde.formfiller.business.DriverManager.initDriverHeadless;

//@SpringBootApplication
public class Main {

    public static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {
        //SpringApplication.run(Main.class, args);

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
        Section2FormInputs section2FormInputs = new Section2FormInputs("163", "1", "2", ServiceType.EXTEND_A_RESIDENCE_TITLE, VisaExtensionForEducationalPurposeVisaEnum_DE.PURPOSE_OF_STUDYING);
        String firstName = "firstname";
        String lastName = "lastname";
        String email = "yilmazn.aslan@gmail.com";
        String birthdate = "11.11.1993";
        Optional<String> residencePermitId = Optional.of("D12123123");
        Optional<Boolean> isPresent = Optional.empty();

        Section4FormInputs section4FormInputs = new Section4FormInputs(firstName, lastName, email, birthdate, isPresent, residencePermitId, ServiceType.EXTEND_A_RESIDENCE_TITLE);


        RemoteWebDriver remoteWebDriver = initDriverHeadless();
        TerminFinder terminFinder = new TerminFinder(section4FormInputs, section2FormInputs, remoteWebDriver);
        terminFinder.startScanning();

        ThreadMonitor threadMonitor = new ThreadMonitor();
        threadMonitor.startMonitoring();

        /*
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutdown hook ran!");
            terminFinder.cancel();
            terminFinder.getDriver().quit();
            logger.info("quitted the driver");
            remoteWebDriver.quit();
        }
        ));

         */



        while (true) {
            Thread.sleep(1000);
        }


    }

    /*
    public static List<Section2FormInputs> generateForms() {
        List<Section2FormInputs> result = new ArrayList<>();
        for (EconomicActivityVisaDe ecoVisa : EconomicActivityVisaDe.values()) {
            result.add(new Section2FormInputs("163", "1", "2", serviceType, ecoVisa));
        }
        return result;
    }

     */

}