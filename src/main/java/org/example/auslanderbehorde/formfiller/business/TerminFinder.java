package org.example.auslanderbehorde.formfiller.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.auslanderbehorde.formfiller.model.PersonalInfoFormTO;
import org.example.auslanderbehorde.formfiller.model.Section4FormInputs;
import org.example.auslanderbehorde.formfiller.model.VisaFormTO;
import org.example.auslanderbehorde.sessionfinder.business.SessionFinder;
import org.example.auslanderbehorde.sessionfinder.model.SessionInfo;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class TerminFinder extends TimerTask {

    private final Logger logger = LogManager.getLogger(TerminFinder.class);
    private final Section4FormInputs section4FormInputs;
    private final VisaFormTO visaFormTO;
    private final PersonalInfoFormTO personalInfoFormTO;
    private final long FORM_REFRESH_PERIOD_MILLISECONDS = 1000;
    private RemoteWebDriver driver;
    private String currentWindowHandle;
    private SessionInfo sessionInfo;
    private final Timer timer = new Timer(true);

    public TerminFinder(Section4FormInputs section4FormInputs, PersonalInfoFormTO personalInfoFormTO, VisaFormTO visaFormTO, RemoteWebDriver driver) {
        this.section4FormInputs = section4FormInputs;
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
        try {
            Section2ServiceSelectionBAL section2ServiceSelectionBAL = new Section2ServiceSelectionBAL(visaFormTO, personalInfoFormTO, driver);
            section2ServiceSelectionBAL.fillAndSendForm();
            driver = section2ServiceSelectionBAL.getDriver();
        } catch (Exception e) {
            logger.info("Exception occurred during handling section 2, quitting.", e);
            return;
        }

        // Section 3
        try {
            Section3DateSelectionBAL section3DateSelectionBAL = new Section3DateSelectionBAL(driver);
            if (section3DateSelectionBAL.isCalenderOpened()) {
                section3DateSelectionBAL.fillAndSendForm();
                driver = section3DateSelectionBAL.getDriver();
            } else {
                logger.info("Page section 3 is not openned, quitting.");
                return;
            }
        } catch (Exception e) {
            logger.info("Exception occurred during handling section 3, quitting.");
            return;
        }

        // Section 4
        Section4Filler section4Filler = new Section4Filler(section4FormInputs, driver);
        try {
            section4Filler.fillAndSendForm();
            driver = section4Filler.getDriver();
        } catch (Exception e) {
            logger.info("Exception occurred during handling section 4, quitting.", e);
            driver = section4Filler.getDriver();
            String fileName = section4Filler.getClass().getSimpleName() + "_exception";
            FormFillerUtils.saveSourceCodeToFile(driver.getPageSource(), fileName);
            FormFillerUtils.saveScreenshot(driver, fileName);
            return;
        }

        // Section 5
        try {
            Section5ReservationBAL section5ReservationBAL = new Section5ReservationBAL(driver);
            section5ReservationBAL.sendForm();
            driver = section5ReservationBAL.getDriver();
            driver.quit();
            timer.cancel();
            return;
        } catch (Exception e) {
            logger.info("Exception occurred during handling section 5, quitting.");
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
