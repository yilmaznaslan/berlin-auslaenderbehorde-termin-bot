package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.auslanderbehorde.formfiller.model.PersonalInfoDTO;

import static org.example.auslanderbehorde.formfiller.business.FormManager.createDummyPersonalInfoDTA;
import static org.example.auslanderbehorde.formfiller.business.FormManager.startForm;

//@SpringBootApplication
public class Main {
    public static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {
        //SpringApplication.run(Main.class, args);
        PersonalInfoDTO personalInfoDTO = createDummyPersonalInfoDTA();
        startForm(personalInfoDTO);
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
}