package org.example.auslanderbehorde.formfiller.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.auslanderbehorde.formfiller.model.PersonalInfoFormTO;
import org.example.auslanderbehorde.formfiller.model.VisaFormTO;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.Timer;
import java.util.TimerTask;

import static org.example.auslanderbehorde.formfiller.business.FormFillerUtils.saveSourceCodeToFile;

public class TerminFinder extends TimerTask {

    private final Logger logger = LogManager.getLogger(TerminFinder.class);
    private final VisaFormTO visaFormTO;
    private final PersonalInfoFormTO personalInfoFormTO;
    private final long FORM_REFRESH_PERIOD_MILLISECONDS = 1000;
    private RemoteWebDriver driver;
    private final Timer timer = new Timer(true);

    public TerminFinder(PersonalInfoFormTO personalInfoFormTO, VisaFormTO visaFormTO, RemoteWebDriver driver) {
        this.personalInfoFormTO = personalInfoFormTO;
        this.visaFormTO = visaFormTO;
        this.driver = driver;
    }

    public void startScanning() {
        logger.info(String.format("Scheduled the task at rate: %s", FORM_REFRESH_PERIOD_MILLISECONDS));
        timer.scheduleAtFixedRate(this, 2000, FORM_REFRESH_PERIOD_MILLISECONDS);
    }

    public void run() {
        // Section 1
        try {
            getFormPage();
        } catch (Exception e) {
            logger.error("Error in initializing a new session. Exception: ", e);
            driver = DriverManager.initDriverHeadless();
            return;
        }

        // Section 1
        Section1MainPageHandler section1MainPageHandler = new Section1MainPageHandler(driver);
        try {
            section1MainPageHandler.fillAndSendForm();
            driver = section1MainPageHandler.getDriver();
        } catch (Exception e) {
            logger.error("Error in initializing a new session. Exception: ", e);
            driver = DriverManager.initDriverHeadless();
            return;
        }

        // Section 2
        Section2ServiceSelectionHandler section2ServiceSelectionHandler = new Section2ServiceSelectionHandler(visaFormTO, personalInfoFormTO, driver);
        try {
            section2ServiceSelectionHandler.fillAndSendForm();
            driver = section2ServiceSelectionHandler.getDriver();
        } catch (Exception e) {
            logger.error("Exception occurred during handling section 2, quitting.", e);
            driver = section2ServiceSelectionHandler.getDriver();
            String fileName = section2ServiceSelectionHandler.getClass().getSimpleName();
            saveSourceCodeToFile(driver.getPageSource(), fileName, "exception");
            FormFillerUtils.saveScreenshot(driver, fileName, "exception");
            return;
        }

        // Section 3
        Section3DateSelectionHandler section3DateSelectionHandler = new Section3DateSelectionHandler(driver);
        try {
            if (section3DateSelectionHandler.isCalenderFound()) {
                logger.info("Calender section is opened");
                section3DateSelectionHandler.fillAndSendForm();
                driver = section3DateSelectionHandler.getDriver();
            } else {
                logger.info("Page section 3 is not opened, quitting.");
                return;
            }
        } catch (Exception e) {
            logger.error("Exception occurred during handling section 3, quitting.", e);
            driver = section3DateSelectionHandler.getDriver();
            String fileName = section3DateSelectionHandler.getClass().getSimpleName();
            saveSourceCodeToFile(driver.getPageSource(), fileName, "exception");
            FormFillerUtils.saveScreenshot(driver, fileName, "exception");
            return;
        }

        // Section 4
        Section4VisaFormHandler section4VisaFormHandler = new Section4VisaFormHandler(personalInfoFormTO, visaFormTO, driver);
        try {
            section4VisaFormHandler.fillAndSendForm();
            driver = section4VisaFormHandler.getDriver();
        } catch (Exception e) {
            logger.error("Exception occurred during handling section 4, quitting.", e);
            driver = section4VisaFormHandler.getDriver();
            String fileName = section4VisaFormHandler.getClass().getSimpleName();
            saveSourceCodeToFile(driver.getPageSource(), fileName, "exception");
            FormFillerUtils.saveScreenshot(driver, fileName, "exception");
            return;
        }

        // Section 5
        Section5ReservationHandler section5ReservationHandler = new Section5ReservationHandler(driver);
        try {
            section5ReservationHandler.sendForm();
            driver = section5ReservationHandler.getDriver();
            driver.quit();
            timer.cancel();
            return;
        } catch (Exception e) {
            logger.error("Exception occurred during handling section 5, quitting.");
            driver = section5ReservationHandler.getDriver();
            String fileName = section5ReservationHandler.getClass().getSimpleName();
            saveSourceCodeToFile(driver.getPageSource(), fileName, "exception");
            FormFillerUtils.saveScreenshot(driver, fileName, "exception");
            return;
        }

    }


    private void getFormPage() {
        String url = "https://otv.verwalt-berlin.de/ams/TerminBuchen?lang=en";
        logger.info(String.format("Getting the URL: %s", url));
        driver.get(url);
    }


    public RemoteWebDriver getDriver() {
        return driver;
    }
}
