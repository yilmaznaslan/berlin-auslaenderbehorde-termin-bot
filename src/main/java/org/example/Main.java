package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.auslanderbehorde.formfiller.model.PersonalInfoFormTO;
import org.example.auslanderbehorde.formfiller.model.VisaFormTO;

import static org.example.auslanderbehorde.formfiller.business.FormManager.*;

//@SpringBootApplication
public class Main {
    public static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {
        //SpringApplication.run(Main.class, args);
        PersonalInfoFormTO personalInfoFormTO = readPersonalInfoFromFile();
        VisaFormTO visaFormTO = readVisaInfoFromFile();
        if(isResidenceTitleInfoVerified(visaFormTO)){
            logger.info("Successfully validated form: {}", visaFormTO);
            startForm(personalInfoFormTO, visaFormTO);
        }else{
            logger.error("Failed validate form: {}", visaFormTO);
        }
        ThreadMonitor threadMonitor = new ThreadMonitor();
        threadMonitor.startMonitoring();
        while (true) {
            Thread.sleep(1000);
        }
    }
}