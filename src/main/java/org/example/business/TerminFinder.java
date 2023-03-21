package org.example.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.formhandlers.*;
import org.example.model.PersonalInfoFormTO;
import org.example.model.VisaFormTO;
import org.example.utils.DriverManager;
import org.example.utils.FormFillerUtils;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import static org.example.utils.DriverManager.initDriverHeadless;
import static org.example.utils.FormFillerUtils.saveSourceCodeToFile;

public class TerminFinder extends TimerTask {

    private final Logger logger = LogManager.getLogger(TerminFinder.class);
    private final VisaFormTO visaFormTO;
    private final PersonalInfoFormTO personalInfoFormTO;
    private final long FORM_REFRESH_PERIOD_MILLISECONDS = 1000;
    private RemoteWebDriver driver;
    private String currentWindowHandle;
    private final Timer timer = new Timer(true);

    public TerminFinder(PersonalInfoFormTO personalInfoFormTO, VisaFormTO visaFormTO, RemoteWebDriver driver) {
        this.personalInfoFormTO = personalInfoFormTO;
        this.visaFormTO = visaFormTO;
        this.driver = driver;
    }

    public TerminFinder(PersonalInfoFormTO personalInfoFormTO, VisaFormTO visaFormTO) {
        this.personalInfoFormTO = personalInfoFormTO;
        this.visaFormTO = visaFormTO;
        this.driver = initDriverHeadless();
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
            try {
                driver = DriverManager.initDriverHeadless();
            } catch (Exception ex) {
                logger.error("Failed to initialize the driver. Quitting. Reason: ", ex);
                driver.quit();
                return;
            }
            return;
        }

        // Section 1
        Section1MainPageHandler section1MainPageHandler = new Section1MainPageHandler(driver);
        try {
            section1MainPageHandler.fillAndSendForm();
            driver = section1MainPageHandler.getDriver();
        } catch (Exception e) {
            logger.error("Error in initializing a new session. Exception: ", e);
            //driver = DriverManager.initDriverHeadless();
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


    private void getFormPage() throws InterruptedException {
        currentWindowHandle = driver.getWindowHandle();
        logger.info("Switching to a new tab");
        driver.switchTo().newWindow(WindowType.TAB);
        Thread.sleep(2000);

        currentWindowHandle = driver.getWindowHandle();

        String url = "https://otv.verwalt-berlin.de/ams/TerminBuchen?lang=en";
        logger.info(String.format("Getting the URL: %s", url));
        Set<String> handle = driver.getWindowHandles();
        handle.forEach((asd) -> logger.info(String.format("Window handle: " + asd)));
        logger.info(String.format("Closing the  window handle: %s", handle.stream().collect(Collectors.toList()).get(0)));
        driver.switchTo().window(handle.stream().collect(Collectors.toList()).get(0)).close();
        logger.info(String.format("Switching to window handle: %s", currentWindowHandle));
        driver.switchTo().window(currentWindowHandle);


        currentWindowHandle = driver.getWindowHandle();
        driver.get(url);

    }


    public RemoteWebDriver getDriver() {
        return driver;
    }
}
