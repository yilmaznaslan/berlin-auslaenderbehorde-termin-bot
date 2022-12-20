package org.example.auslanderbehorde.formfiller.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.auslanderbehorde.appointmentfinder.business.AppointmentFinder;
import org.example.auslanderbehorde.formfiller.enums.VisaEnum;
import org.example.auslanderbehorde.formfiller.exceptions.ElementNotFoundException;
import org.example.auslanderbehorde.formfiller.exceptions.InteractionFailedException;
import org.example.auslanderbehorde.formfiller.model.FormInputs;
import org.example.auslanderbehorde.sessionfinder.business.SessionFinder;
import org.example.auslanderbehorde.sessionfinder.model.SessionModel;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static org.example.auslanderbehorde.appointmentfinder.business.AppointmentFinder.foundAppointmentCount;
import static org.example.auslanderbehorde.formfiller.business.FormFillerUtils.SLEEP_DURATION_IN_MILISECONDS;
import static org.example.auslanderbehorde.formfiller.business.FormFillerUtils.TIMEOUT_FOR_INTERACTING_IN_SECONDS;
import static org.example.auslanderbehorde.formfiller.enums.FormParameterEnum.*;

/**
 * Business Access Layer for filling the form
 */
public class FormFillerBAL extends TimerTask {

    private final Logger logger = LogManager.getLogger(FormFillerBAL.class);
    public static final int FORM_REFRESH_PERIOD_MILISECONDS = 1000;

    private final String citizenshipValue;
    private final String applicantNumber;
    private final String familyStatus;
    private final VisaEnum visaEnum;

    private final AppointmentFinder appointmentFinder;
    private final SessionModel sessionModel;
    private static int searchCount = 0;

    private WebDriver driver;
    private final Timer timer = new Timer(true);


    public void startScanning() {
        timer.scheduleAtFixedRate(this, 0, FORM_REFRESH_PERIOD_MILISECONDS);
    }

    public FormFillerBAL(SessionModel sessionModel, FormInputs formInputs, WebDriver webDriver) {
        this.sessionModel = sessionModel;
        this.driver = webDriver;
        FormFillerUtils.formId = new Random().nextLong();
        this.citizenshipValue = formInputs.getCitizenshipValue();
        this.applicantNumber = formInputs.getApplicationsNumber();
        this.familyStatus = formInputs.getFamilyStatus();
        this.visaEnum = formInputs.getVisaEnum();
        this.appointmentFinder = new AppointmentFinder(webDriver);
    }

    @Override
    public void run() {
        try {
            goToFormPage(sessionModel.getRequestId(), sessionModel.getDswid(), sessionModel.getDsrid());
            double remainingMinute = getRemainingTime();

            if (remainingMinute <= 1) {
                logger.warn("Time is up");
                SessionFinder sessionFinder = new SessionFinder();
                sessionFinder.findRequestId();
                sessionModel.setRequestId(sessionFinder.getSessionModel().getRequestId());
                sessionModel.setDswid(sessionFinder.getSessionModel().getDswid());
                sessionModel.setDsrid(sessionFinder.getSessionModel().getDsrid());
            }

            selectCitizenshipValue();
            selectApplicantsCount();
            selectFamilyStatus();
            clickServiceType();
            clickVisaGroup();
            clickToVisa();
            clickNextButton();

            if (isAppointmentSelectionPageOpened()) {
                Thread.sleep(1000);
                appointmentFinder.handleFindingAppointment();
                //timer.cancel();
            }

            searchCount = searchCount + 1;
            String msg = String.format("Completed search count: %s. Found count: %s", searchCount, foundAppointmentCount);
            logger.info(msg);

        } catch (Exception e) {
            logger.warn("Some error occurred. Reason ", e);
            driver.close();
            driver = initDriverHeadless();
        }

    }

    public void goToFormPage(String requestId, String dswid, String dsrid) {
        String hostUrl = "https://otv.verwalt-berlin.de/ams/TerminBuchen/wizardng";
        String targetUrl = hostUrl + "/" + requestId + "?dswid=" + dswid + "&dsrid=" + dsrid;
        logger.info(String.format("Finding the URL: %s", targetUrl));
        driver.get(targetUrl);
    }

    public WebDriver initDriverHeadless() {
        logger.info("Initializing driver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-logging");
        options.addArguments("--headless");
        return new ChromeDriver(options);
    }

    private void selectCitizenshipValue() throws InterruptedException, ElementNotFoundException {
        String elementName = COUNTRY.getId();
        String elementDescription = COUNTRY.name();
        WebElement element = FormFillerUtils.getElementById(elementName, elementDescription, driver);
        FormFillerUtils.selectOptionByValue(element, elementDescription, citizenshipValue);
    }

    private void selectApplicantsCount() throws InterruptedException, ElementNotFoundException {
        String elementId = APPLICANT_COUNT.getId();
        String elementDescription = APPLICANT_COUNT.name();
        WebElement element = FormFillerUtils.getElementById(elementId, elementDescription, driver);
        FormFillerUtils.selectOptionByValue(element, elementDescription, applicantNumber);
    }

    private void selectFamilyStatus() throws InterruptedException, ElementNotFoundException {
        String elementId = FAMILY_STATUS.getId();
        String elementDescription = FAMILY_STATUS.name();
        WebElement element = FormFillerUtils.getElementById(elementId, elementDescription, driver);
        FormFillerUtils.selectOptionByValue(element, elementDescription, familyStatus);
    }

    private void clickServiceType() throws InterruptedException, ElementNotFoundException, InteractionFailedException {
        String elementXPath = "//*[@id=\"xi-div-30\"]/div[1]/label/p";
        String elementDescription = "serviceType".toUpperCase();
        WebElement element = FormFillerUtils.getElementByXPath(elementXPath, elementDescription, driver);
        FormFillerUtils.clickToElement(element, elementDescription);
    }

    private void clickVisaGroup() throws InterruptedException, ElementNotFoundException, InteractionFailedException {
        String elementXpath = "//*[@id=\"inner-163-0-1\"]/div/div[3]/label";
        String elementDescription = "visaGroup".toUpperCase();
        WebElement element = FormFillerUtils.getElementByXPath(elementXpath, elementDescription, driver);
        FormFillerUtils.clickToElement(element, elementDescription);
    }

    private void clickToVisa() throws InterruptedException, ElementNotFoundException, InteractionFailedException {
        String elementId = visaEnum.getId();
        String elementDescription = visaEnum.getDataTag0();
        WebElement element = FormFillerUtils.getElementById(elementId, elementDescription, driver);
        FormFillerUtils.clickToElement(element, elementDescription);
    }

    private void clickNextButton() throws InterruptedException, ElementNotFoundException, InteractionFailedException {
        String elementXpath = "//*[@id=\"applicationForm:managedForm:proceed\"]";
        String elementDescription = "clickButton".toUpperCase();
        WebElement element = FormFillerUtils.getElementByXPath(elementXpath, elementDescription, driver);
        FormFillerUtils.clickToElement(element, elementDescription);
    }

    private boolean isAppointmentSelectionPageOpened() {
        String stageXPath = ".//ul/li[2]/span";
        String elementDescription = "activeSectionTab".toUpperCase();
        String stageText = "";
        int i = 0;
        while (i <= TIMEOUT_FOR_INTERACTING_IN_SECONDS) {
            try {
                WebElement element = FormFillerUtils.getElementByXPath(stageXPath, elementDescription, driver);
                stageText = element.getText();
                logger.info(String.format("Element: %s. Process: Getting Text. Status: Successfully. Value: %s", elementDescription, stageText));
                Thread.sleep(SLEEP_DURATION_IN_MILISECONDS);
                break;
            } catch (Exception e) {
                logger.info(String.format("Element: %s. Process: Getting Text. Status: Failed", elementDescription));
            }
            i++;
        }
        if (i > TIMEOUT_FOR_INTERACTING_IN_SECONDS) {
            logger.warn(String.format("Element: %s. Process: reading text, Result: Failed Reason: Couldn't select within timeout", elementDescription));
        }
        return stageText.contains("Terminauswahl");
    }

    private double getRemainingTime() throws InterruptedException, ElementNotFoundException {
        String elementXpath = "//*[@id=\"progressBar\"]/div";
        String elementDescription = "remainingTime".toUpperCase();
        WebElement timeBar = FormFillerUtils.getElementByXPath(elementXpath, elementDescription, driver);
        int remainingMinute = 0;
        try {
            String timeStr = timeBar.getText();
            if (timeStr != null) {
                remainingMinute = Integer.parseInt(timeStr.split(":")[0]);
            }
            logger.info(String.format("Element: %s. Process: Getting time. Status: Successfully. Value: %s", elementDescription, timeStr));
        } catch (Exception e) {
            logger.info("Element: {}. Process: Getting time. Status: Failed", elementDescription);
        }
        return remainingMinute;
    }

}
