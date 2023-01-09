package org.example.auslanderbehorde.formfiller.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.auslanderbehorde.formfiller.enums.SeleniumProcessEnum;
import org.example.auslanderbehorde.formfiller.enums.SeleniumProcessResultEnum;
import org.example.auslanderbehorde.formfiller.exceptions.ElementNotFoundTimeoutException;
import org.example.auslanderbehorde.formfiller.exceptions.InteractionFailedException;
import org.example.auslanderbehorde.formfiller.model.FormInputs;
import org.example.auslanderbehorde.formfiller.model.Section4FormInputs;
import org.example.auslanderbehorde.sessionfinder.business.SessionFinder;
import org.example.auslanderbehorde.sessionfinder.model.SessionInfo;
import org.example.notifications.Helper;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import static org.example.auslanderbehorde.formfiller.business.FormFillerUtils.TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS;
import static org.example.auslanderbehorde.formfiller.business.FormFillerUtils.logInfo;
import static org.example.auslanderbehorde.formfiller.business.Section3DateSelectionBAL.foundAppointmentCount;
import static org.example.auslanderbehorde.formfiller.business.Section3DateSelectionBAL.handledAppointmentCount;

/**
 * Business Access Layer for filling the form
 */
public class MainLogic extends TimerTask {

    private final Logger logger = LogManager.getLogger(MainLogic.class);
    public static final int FORM_REFRESH_PERIOD_MILLISECONDS = 1000;

    private SessionInfo sessionInfo;
    private static int searchCount = 0;
    private static int succesfullyFormSentCount = 0;

    private RemoteWebDriver driver;
    private final Timer timer = new Timer(true);
    String currentWindowHandle;

    private final Section2ServiceSelectionBAL section2ServiceSelection;
    private final Section3DateSelectionBAL section3DateSelectionBAL;
    private final Section4DetailsBAL section4DetailsBAL;


    public MainLogic(FormInputs formInputs, Section4FormInputs section4FormInputs, RemoteWebDriver remoteWebDriver) {
        this.driver = remoteWebDriver;
        this.section2ServiceSelection = new Section2ServiceSelectionBAL(formInputs, remoteWebDriver);
        this.section3DateSelectionBAL = new Section3DateSelectionBAL(remoteWebDriver);
        this.section4DetailsBAL = new Section4DetailsBAL(section4FormInputs, remoteWebDriver);
    }

    public void startScanning() {
        logger.info(String.format("Scheduled the task at rate: %s", FORM_REFRESH_PERIOD_MILLISECONDS));
        timer.scheduleAtFixedRate(this, 2000, FORM_REFRESH_PERIOD_MILLISECONDS);
    }

    @Override
    public void run() {
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
        try {

            String firstName = "firstName";
            String lastName = "lastname";
            String email = "yilmazn.aslan@gmail.com";
            String birthdate = "12.03.1993";
            Section4FormInputs form = new Section4FormInputs(firstName, lastName, email, birthdate, true);

            getFormPage(sessionInfo.getRequestId(), sessionInfo.getDswid(), sessionInfo.getDsrid());
            section2ServiceSelection.fillAndSendForm();
            if (isCalenderOpened()) {
                succesfullyFormSentCount++;
                Thread.sleep(1000);
                Section3DateSelectionBAL section3DateSelectionBAL = new Section3DateSelectionBAL(driver);
                section3DateSelectionBAL.handleFindingAppointment();
            }
            clickToSelectService();
            searchCount++;
            String msg = String.format("Completed search count: %s. SuccessfullyFormSenCount:%s, HandledAppoi.Count:%s, Found count: %s", searchCount, succesfullyFormSentCount, handledAppointmentCount, foundAppointmentCount);
            logger.info(msg);

        } catch (Exception e) {
            logger.warn("Some error occurred. Reason ", e);
            //driver.close();
            try {
                initNewSessionInfo();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }

    }

    public void getFormPage(String requestId, String dswid, String dsrid) {
        String hostUrl = "https://otv.verwalt-berlin.de/ams/TerminBuchen/wizardng";
        String targetUrl = hostUrl + "/" + requestId + "?dswid=" + dswid + "&dsrid=" + dsrid;
        logger.info(String.format("Getting the URL: %s", targetUrl));
        currentWindowHandle = driver.getWindowHandle();
        driver.get(targetUrl);
    }

    private void clickToSelectService() throws ElementNotFoundTimeoutException, InterruptedException, InteractionFailedException {
        String xpath = "//*[@id=\"main\"]/div[2]/div[4]/div[2]/div/div[1]/ul/li[1]";
        String elementDescription = "Select Service";
        WebElement element = FormFillerUtils.getElementByXPath(xpath, elementDescription, driver);
        FormFillerUtils.clickToElement(element, elementDescription);
        //String alert = driver.switchTo().alert().getText();
        //driver.switchTo().alert().dismiss();
        //logger.info("Clicked to dismiss");
    }

    protected boolean isCalenderOpened() throws InteractionFailedException {
        String stageXPath = ".//ul/li[2]/span";
        String elementDescription = "activeSectionTab".toUpperCase();
        int i = 1;
        String stageText;
        while (i <= TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS) {
            try {
                WebElement element = FormFillerUtils.getElementByXPath(stageXPath, elementDescription, driver);
                stageText = element.getText();
                logInfo(elementDescription, SeleniumProcessEnum.GETTING_TEXT, SeleniumProcessResultEnum.SUCCESSFUL.name(), String.format("Value: %s", stageText));
                String asd = "//*[@id=\"xi-div-1\"]/div[3]";
                try {
                    String elementDescription1 = "calender".toUpperCase();
                    WebElement calender = FormFillerUtils.getElementByXPathCalender(asd, elementDescription1, driver);
                    FormFillerUtils.saveSourceCodeToFile(driver.getPageSource(), "dateSelection_in");
                    FormFillerUtils.saveScreenshot(driver, "dateSelection_in");
                    return true;
                    //return stageText.contains("Terminauswahl") && calender.isDisplayed();
                } catch (ElementNotFoundTimeoutException e) {
                    return false;
                }

            } catch (StaleElementReferenceException | InterruptedException | ElementNotFoundTimeoutException e) {
                //logWarn(elementDescription, SeleniumProcessEnum.GETTING_TEXT.name(), SeleniumProcessResultEnum.FAILED.name(), e);
            }
            i++;
        }
        if (i > TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS) {
            logger.warn("Element: {}. Process: Getting by elementXPath. Result: Failed. Reason: Couldn't get the element within timeout", elementDescription);
            throw new InteractionFailedException(elementDescription);

        }
        return false;
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
    }

    public RemoteWebDriver getDriver() {
        return this.driver;
    }
}
