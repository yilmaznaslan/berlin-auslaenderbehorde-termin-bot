package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.auslanderbehorde.formfiller.business.TerminFinder;
import org.example.auslanderbehorde.formfiller.enums.EconomicActivityVisaDeEnum;
import org.example.auslanderbehorde.formfiller.enums.ResidencePermitExtensionEducationalPurposeVisaEnum_DE;
import org.example.auslanderbehorde.formfiller.model.FormInputs;
import org.example.auslanderbehorde.formfiller.model.Section4FormInputs;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.example.auslanderbehorde.formfiller.business.DriverManager.initDriverHeadless;

@SpringBootApplication
public class Main {

    public static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(Main.class, args);

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
        FormInputs formInputs = new FormInputs("163", "1", "2", ResidencePermitExtensionEducationalPurposeVisaEnum_DE.PURPOSE_OF_STUDYING);
        String firstName = "Gizem";
        String lastName = "Sacihan";
        String email = "yilmazn.aslan@gmail.com";
        String birthdate = "16.01.1994";
        String residencePermitId = "D42643052";
        Section4FormInputs section4FormInputs = new Section4FormInputs(firstName, lastName, email, birthdate, true, Optional.of(residencePermitId));

        RemoteWebDriver remoteWebDriver = initDriverHeadless();
        TerminFinder terminFinder = new TerminFinder(section4FormInputs, formInputs, remoteWebDriver);
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


        /*
        while (true) {
            Thread.sleep(1000);
        }

         */
    }

    public static List<FormInputs> generateForms() {
        List<FormInputs> result = new ArrayList<>();
        for (EconomicActivityVisaDeEnum ecoVisa : EconomicActivityVisaDeEnum.values()) {
            result.add(new FormInputs("163", "1", "2", ecoVisa));
        }
        return result;
    }

}