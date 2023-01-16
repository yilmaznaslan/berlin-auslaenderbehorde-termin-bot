package org.example.auslanderbehorde.formfiller.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.auslanderbehorde.formfiller.model.FormInputs;
import org.example.auslanderbehorde.formfiller.model.Section4FormInputs;
import org.example.auslanderbehorde.sessionfinder.business.SessionFinder;
import org.example.auslanderbehorde.sessionfinder.model.SessionInfo;
import org.example.notifications.Helper;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import static org.example.auslanderbehorde.formfiller.business.Section2ServiceSelection.FORM_REFRESH_PERIOD_MILLISECONDS;

public class TerminFinder extends TimerTask {

    private final Logger logger = LogManager.getLogger(TerminFinder.class);
    private final Section4FormInputs section4FormInputs;
    private final FormInputs formInputs;
    private RemoteWebDriver driver;
    private String currentWindowHandle;
    private SessionInfo sessionInfo;
    private final Timer timer = new Timer(true);

    public TerminFinder(Section4FormInputs section4FormInputs, FormInputs formInputs, RemoteWebDriver driver) {
        this.section4FormInputs = section4FormInputs;
        this.formInputs = formInputs;
        this.driver = driver;
    }

    public void startScanning() {
        logger.info(String.format("Scheduled the task at rate: %s", FORM_REFRESH_PERIOD_MILLISECONDS));
        timer.scheduleAtFixedRate(this, 2000, FORM_REFRESH_PERIOD_MILLISECONDS);
    }

    public void run() {
        // Section 1
        int initSessionTryCount = 0;
        try {
            while (true) {
                initSessionTryCount++;
                initNewSessionInfo();
                break;
            }
        } catch (Exception e) {
            logger.error("Session initialization had failed. initSessionTryCount: {} Exception: ", initSessionTryCount, e);
            driver.quit();
            driver = Helper.initDriverHeadless();
        }

        // Section 2
        try {
            Section2ServiceSelectionBAL section2ServiceSelectionBAL = new Section2ServiceSelectionBAL(formInputs, driver);
            section2ServiceSelectionBAL.fillAndSendForm();
            driver = section2ServiceSelectionBAL.getDriver();
        } catch (Exception e) {
            logger.info("Exception occurred during handling section 2, quitting.");
            return;
            // init the whole process
        }

        // Section 3
        try {
            Section3DateSelectionBAL section3DateSelectionBAL = new Section3DateSelectionBAL(driver);
            section3DateSelectionBAL.fillAndSendForm();

        } catch (Exception e) {
            logger.info("Exception occurred during handling section 3, quitting.");
            return;
            // init the whole process
        }

        // Section 4
        try {
            Section4DetailsBAL section4DetailsBAL = new Section4DetailsBAL(section4FormInputs, driver);
            section4DetailsBAL.fillAndSendForm();
            driver = section4DetailsBAL.getDriver();
        } catch (Exception e) {
            logger.info("Exception occurred during handling section 4, quitting.");
            return;
            // init the whole process
        }

        // Section 5
        try {
            Section5ReservationBAL section5ReservationBAL = new Section5ReservationBAL(driver);
            section5ReservationBAL.sendForm();
            driver = section5ReservationBAL.getDriver();
        } catch (Exception e) {
            logger.info("Exception occurred during handling section 5, quitting.");
            return;
            // init the whole process
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
        logger.info(String.format("Closing the  window handle: %s", handle.stream().toList().get(0)));
        driver.switchTo().window(handle.stream().toList().get(0)).close();
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
}
