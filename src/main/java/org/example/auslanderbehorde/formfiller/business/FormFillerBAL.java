package org.example.auslanderbehorde.formfiller.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.auslanderbehorde.appointmentfinder.business.AppointmentFinder;
import org.example.auslanderbehorde.formfiller.enums.SeleniumProcessEnum;
import org.example.auslanderbehorde.formfiller.enums.SeleniumProcessResultEnum;
import org.example.auslanderbehorde.formfiller.enums.VisaEnum;
import org.example.auslanderbehorde.formfiller.exceptions.ElementNotFoundTimeoutException;
import org.example.auslanderbehorde.formfiller.exceptions.InteractionFailedException;
import org.example.auslanderbehorde.formfiller.model.FormInputs;
import org.example.auslanderbehorde.sessionfinder.business.SessionFinder;
import org.example.auslanderbehorde.sessionfinder.model.SessionInfo;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static org.example.auslanderbehorde.appointmentfinder.business.AppointmentFinder.foundAppointmentCount;
import static org.example.auslanderbehorde.appointmentfinder.business.AppointmentFinder.handledAppointmentCount;
import static org.example.auslanderbehorde.formfiller.business.FormFillerUtils.*;
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

    private SessionInfo sessionInfo;
    private static int searchCount = 0;
    private static int succesfullyFormSentCount = 0;

    private WebDriver driver;
    private final Timer timer = new Timer(true);

    public void startScanning() {
        timer.scheduleAtFixedRate(this, 0, FORM_REFRESH_PERIOD_MILISECONDS);
    }

    public FormFillerBAL(FormInputs formInputs, WebDriver webDriver) {
        this.driver = webDriver;
        FormFillerUtils.formId = new Random().nextLong();
        this.citizenshipValue = formInputs.getCitizenshipValue();
        this.applicantNumber = formInputs.getApplicationsNumber();
        this.familyStatus = formInputs.getFamilyStatus();
        this.visaEnum = formInputs.getVisaEnum();
    }

    @Override
    public void run() {
        try {
            if(sessionInfo ==null){
                logger.info("Session is not created.");
                initNewSession();
            }
            getFormPage(sessionInfo.getRequestId(), sessionInfo.getDswid(), sessionInfo.getDsrid());

            double remainingMinute = getRemainingTime();

            if (remainingMinute <= 1) {
                logger.warn("Time is up");
                initNewSession();
            }

            selectCitizenshipValue();
            selectApplicantsCount();
            selectFamilyStatus();
            clickServiceType();
            clickVisaGroup();
            clickToVisa();
            sendForm();

            if (isCalenderOpened()) {
                succesfullyFormSentCount++;
                Thread.sleep(1000);
                AppointmentFinder appointmentFinder = new AppointmentFinder(driver);
                appointmentFinder.handleFindingAppointment();
                //timer.cancel();
            }

            searchCount++;
            String msg = String.format("Completed search count: %s. SuccessfullyFormSenCount:%s, HandledAppoi.Count:%s, Found count: %s", searchCount, succesfullyFormSentCount, handledAppointmentCount, foundAppointmentCount);
            logger.info(msg);

        } catch (Exception e) {
            logger.warn("Some error occurred. Reason ", e);
            driver.close();
            driver = initDriverHeadless();
            try {
                initNewSession();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }

    }

    public void getFormPage(String requestId, String dswid, String dsrid) {
        String hostUrl = "https://otv.verwalt-berlin.de/ams/TerminBuchen/wizardng";
        String targetUrl = hostUrl + "/" + requestId + "?dswid=" + dswid + "&dsrid=" + dsrid;
        logger.info(String.format("Getting the URL: %s", targetUrl));
        driver.get(targetUrl);
    }

    public WebDriver initDriverHeadless() {
        logger.info("Initializing driver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-logging");
        options.addArguments("--headless");
        return new ChromeDriver(options);
    }

    private void selectCitizenshipValue() throws InterruptedException, ElementNotFoundTimeoutException {
        String elementName = COUNTRY.getId();
        String elementDescription = COUNTRY.name();
        WebElement element = FormFillerUtils.getElementById(elementName, elementDescription, driver);
        Select select = new Select(element);
        select.selectByValue( citizenshipValue);
        WebElement option = select.getFirstSelectedOption();
        String selectValue = option.getText();
        logInfo(elementDescription, SeleniumProcessEnum.SELECTING_OPTION, "Successful", "value" + selectValue);
    }

    private void selectApplicantsCount() throws InterruptedException, ElementNotFoundTimeoutException, InteractionFailedException {
        String elementId = APPLICANT_COUNT.getId();
        String elementDescription = APPLICANT_COUNT.name();
        WebElement element = FormFillerUtils.getElementById(elementId, elementDescription, driver);
        FormFillerUtils.selectOptionByValue(element, elementDescription, applicantNumber);
        //Select select = new Select(element);
        //select.selectByValue(applicantNumber);
        //WebElement option = select.getFirstSelectedOption();
        //String selectValue = option.getText();
        //logInfo(elementDescription, SeleniumProcessEnum.SELECTING_OPTION, "Successful", "value" + selectValue);
        }

    private void selectFamilyStatus() throws InterruptedException, ElementNotFoundTimeoutException, InteractionFailedException {
        String elementId = FAMILY_STATUS.getId();
        String elementDescription = FAMILY_STATUS.name();
        WebElement element = FormFillerUtils.getElementById(elementId, elementDescription, driver);
        FormFillerUtils.selectOptionByValue(element, elementDescription, familyStatus);
        //Select select = new Select(element);
        //select.selectByValue(familyStatus);
        //WebElement option = select.getFirstSelectedOption();
        //String selectValue = option.getText();
        // logInfo(elementDescription, SeleniumProcessEnum.SELECTING_OPTION, "Successful", "value" + selectValue);
        }

    private void clickServiceType() throws InterruptedException, ElementNotFoundTimeoutException, InteractionFailedException {
        String elementXPath = "//*[@id=\"xi-div-30\"]/div[1]/label/p";
        String elementDescription = "serviceType".toUpperCase();
        WebElement element = FormFillerUtils.getElementByXPath(elementXPath, elementDescription, driver);
        FormFillerUtils.clickToElement(element, elementDescription);
        //element.click();
        //logInfo(elementDescription, SeleniumProcessEnum.CLICKING_TO_ELEMENT, SeleniumProcessResultEnum.SUCCESSFUL.name());
    }

    private void clickVisaGroup() throws InterruptedException, ElementNotFoundTimeoutException, InteractionFailedException {
        String elementXpath = "//*[@id=\"inner-163-0-1\"]/div/div[3]/label";
        String elementDescription = "visaGroup".toUpperCase();
        WebElement element = FormFillerUtils.getElementByXPath(elementXpath, elementDescription, driver);
        FormFillerUtils.clickToElement(element, elementDescription);
        //element.click();
        //logInfo(elementDescription, SeleniumProcessEnum.CLICKING_TO_ELEMENT, SeleniumProcessResultEnum.SUCCESSFUL.name());
    }

    private void clickToVisa() throws InterruptedException, ElementNotFoundTimeoutException, InteractionFailedException {
        String elementId = visaEnum.getId();
        String elementDescription = visaEnum.getDataTag0();
        WebElement element = FormFillerUtils.getElementById(elementId, elementDescription, driver);
        FormFillerUtils.clickToElement(element, elementDescription);
        //element.click();
        //logInfo(elementDescription, SeleniumProcessEnum.CLICKING_TO_ELEMENT, SeleniumProcessResultEnum.SUCCESSFUL.name());
        }

    private void sendForm() throws InterruptedException, ElementNotFoundTimeoutException, InteractionFailedException {
        String elementXpath = "//*[@id=\"applicationForm:managedForm:proceed\"]";
        String elementDescription = "clickButton".toUpperCase();
        WebElement element = FormFillerUtils.getElementByXPath(elementXpath, elementDescription, driver);
        FormFillerUtils.clickToElement(element, elementDescription);
        //element.click();
        //logInfo(elementDescription, SeleniumProcessEnum.CLICKING_TO_ELEMENT, SeleniumProcessResultEnum.SUCCESSFUL.name());
    }

    protected boolean isCalenderOpened() throws InteractionFailedException{
        String stageXPath = ".//ul/li[2]/span";
        String elementDescription = "activeSectionTab".toUpperCase();
        int i = 1;
        String stageText;
        while(i <= TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS){
            try{
                WebElement element = FormFillerUtils.getElementByXPath(stageXPath, elementDescription, driver);
                stageText = element.getText();
                logInfo(elementDescription, SeleniumProcessEnum.GETTING_TEXT, SeleniumProcessResultEnum.SUCCESSFUL.name(), String.format("Value: %s", stageText));
                if(stageText.contains("Servicewahl")){
                }
                String asd = "//*[@id=\"xi-div-1\"]/div[3]";
                try{
                    String elementDescription1 = "calender".toUpperCase();
                    WebElement calender = FormFillerUtils.getElementByXPathCalender(asd,elementDescription1, driver);
                    FormFillerUtils.saveSourceCodeToFile(driver.getPageSource(), "dateSelection_in");
                    FormFillerUtils.saveScreenshot(driver, "dateSelection_in");
                    return true;
                    //return stageText.contains("Terminauswahl") && calender.isDisplayed();
                } catch(ElementNotFoundTimeoutException e){
                    return false;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            } catch(StaleElementReferenceException | InterruptedException | ElementNotFoundTimeoutException e){
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

    private double getRemainingTime() {
        String elementXpath = "//*[@id=\"progressBar\"]/div";
        String elementDescription = "remainingTime".toUpperCase();
        int remainingMinute = 0;
        try {
            WebElement element = FormFillerUtils.getElementByXPath(elementXpath, elementDescription, driver);
            String timeStr = element.getText();
            if (timeStr != null) {
                remainingMinute = Integer.parseInt(timeStr.split(":")[0]);
            }
        } catch (Exception e) {
            logger.info("Element: {}. Process: Getting time. Status: Failed", elementDescription);
        }
        return remainingMinute;
    }

    private void initNewSession() throws InterruptedException {
        logger.info("initiating a new session");
        SessionFinder sessionFinder = new SessionFinder();
        sessionInfo = sessionFinder.findAndGetSession();
    }

}
