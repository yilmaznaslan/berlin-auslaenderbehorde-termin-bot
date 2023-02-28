package org.example.auslanderbehorde.formfiller.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.auslanderbehorde.formfiller.model.PersonalInfoFormTO;
import org.example.auslanderbehorde.formfiller.model.VisaFormTO;
import org.example.auslanderbehorde.sessionfinder.business.SessionFinder;
import org.example.auslanderbehorde.sessionfinder.model.SessionInfo;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import static org.example.auslanderbehorde.formfiller.business.FormFillerUtils.saveSourceCodeToFile;

public class TerminFinder extends TimerTask {

    private final Logger logger = LogManager.getLogger(TerminFinder.class);
    private final VisaFormTO visaFormTO;
    private final PersonalInfoFormTO personalInfoFormTO;
    private final long FORM_REFRESH_PERIOD_MILLISECONDS = 1000;
    private RemoteWebDriver driver;
    private String currentWindowHandle;
    private SessionInfo sessionInfo;
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
            initNewSessionInfo();
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

    private void initNewSessionInfo() throws InterruptedException {
        logger.info("Switching to a new tab");
        driver.switchTo().newWindow(WindowType.TAB);
        Thread.sleep(2000);
        SessionFinder sessionFinder = new SessionFinder(driver);
        sessionInfo = sessionFinder.findAndGetSession();
        currentWindowHandle = sessionFinder.getDriver().getWindowHandle();
        Set<String> handle = driver.getWindowHandles();
        handle.forEach((asd) -> logger.info(String.format("Window handle: " + asd)));
        logger.info(String.format("Closing the  window handle: %s", handle.stream().collect(Collectors.toList()).get(0)));
        driver.switchTo().window(handle.stream().collect(Collectors.toList()).get(0)).close();
        logger.info(String.format("Switching to window handle: %s", currentWindowHandle));
        driver.switchTo().window(currentWindowHandle);
        getFormPage(sessionInfo.getRequestId(), sessionInfo.getDswid(), sessionInfo.getDsrid());
    }

    private void getFormPage(String requestId, String dswid, String dsrid) {
        String hostUrl = "https://otv.verwalt-berlin.de/ams/TerminBuchen/wizardng";
        String targetUrl = hostUrl + "/" + requestId + "?dswid=" + dswid + "&dsrid=" + dsrid;
        logger.info(String.format("Getting the URL: %s", targetUrl));
        currentWindowHandle = driver.getWindowHandle();
        driver.get(targetUrl);
    }

    public RemoteWebDriver getDriver() {
        return driver;
    }
}
