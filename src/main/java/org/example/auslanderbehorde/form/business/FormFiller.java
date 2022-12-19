package org.example.auslanderbehorde.form.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.auslanderbehorde.*;
import org.example.auslanderbehorde.form.exceptions.ElementNotFoundException;
import org.example.auslanderbehorde.form.exceptions.InteractionFailedException;
import org.example.auslanderbehorde.form.model.FormInputs;
import org.example.auslanderbehorde.form.enums.VisaEnum;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static org.example.auslanderbehorde.form.business.FormFillerUtils.SLEEP_DURATION_IN_MILISECONDS;
import static org.example.auslanderbehorde.form.business.FormFillerUtils.TIMEOUT_FOR_INTERACTING_IN_SECONDS;
import static org.example.auslanderbehorde.form.enums.FormParameterEnum.*;
import static org.example.notifications.Twilio.sendSMS;

public class FormFiller extends TimerTask {

    private final Logger logger = LogManager.getLogger(FormFiller.class);
    public static final int FORM_REFRESH_PERIOD_MILISECONDS = 1000;

    private final String citizenshipValue;
    private final String applicantNumber;
    private final String familyStatus;
    private final VisaEnum visaEnum;
    private final long formId;

    private String requestId;
    private String dswid;
    private String dsrid;
    private static int searchCount = 0;
    private static int foundCount = 0;

    private WebDriver driver;
    private Timer timer = new Timer(true);


    public void startScanning() {
        timer.scheduleAtFixedRate(this, 0, FORM_REFRESH_PERIOD_MILISECONDS);
    }

    public FormFiller(String requestId, String dswid, String dsrid, FormInputs formInputs, WebDriver webDriver) {
        this.requestId = requestId;
        this.dswid = dswid;
        this.dsrid = dsrid;
        this.driver = webDriver;
        this.formId = new Random().nextLong();
        FormFillerUtils.formId = formId;
        this.citizenshipValue = formInputs.getCitizenshipValue();
        this.applicantNumber = formInputs.getApplicationsNumber();
        this.familyStatus = formInputs.getFamilyStatus();
        this.visaEnum = formInputs.getVisaEnum();

    }

    @Override
    public void run() {
        try {
            goToFormPage(requestId, dswid, dsrid);
            double remainingMinute = getRemainingTime();

            if (remainingMinute <= 1) {
                logger.warn("Time is up");
                SessionFinder sessionFinder = new SessionFinder();
                sessionFinder.findRequestId();
                this.requestId = sessionFinder.getRequestId();
                this.dswid = sessionFinder.getDswid();
                this.dsrid = sessionFinder.getDsrid();
            }

            selectCitizenshipValue();
            selectApplicantsCount();
            selectFamilyStatus();
            clickServiceType();
            clickVisaGroup();
            clickToVisa();
            clickNextButton();

            if (isResultSuccessful()) {
                Thread.sleep(1000);
                foundCount++;
                String url = driver.getCurrentUrl();
                String msg = String.format("Found a place. URL: %s", url);
                logger.info(msg);
                initDriverWithHead().get(url);
                //FormFillerUtils.saveSourceCodeToFile(driver.getPageSource());
                //FormFillerUtils.saveScreenshot(driver);
                String myPhoneNumber = System.getenv("myPhoneNumber");
                //makeCall(myPhoneNumber);
                sendSMS(myPhoneNumber, url);
                clickToFirstAvailableDate();
                selectFirstAvailableTimeSlot();
                clickWeiterButton();
                //timer.cancel();
            }

            this.searchCount = this.searchCount + 1;
            String msg = String.format("Completed search count: %s. Found Count: %s", searchCount, foundCount);
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

    private WebDriver initDriverWithHead() {
        logger.info("Initializing driver");
        ChromeOptions options = new ChromeOptions();
//        FirefoxOptions options = new FirefoxOptions();

        options.addArguments("--disable-logging");
        //      return new FirefoxDriver();

        return new ChromeDriver(options);
    }

    private void selectCitizenshipValue() throws InterruptedException, ElementNotFoundException {
        String elementName = COUNTRY.getId();
        String elementDescription = COUNTRY.name();
        WebElement element = FormFillerUtils.getElementById(elementName, elementDescription, driver);
        FormFillerUtils.selectOption(element, elementDescription, citizenshipValue);
    }

    private void selectApplicantsCount() throws InterruptedException, ElementNotFoundException {
        String elementId = APPLICANT_COUNT.getId();
        String elementDescription = APPLICANT_COUNT.name();
        WebElement element = FormFillerUtils.getElementById(elementId, elementDescription, driver);
        FormFillerUtils.selectOption(element, elementDescription, applicantNumber);
    }

    protected void selectFirstAvailableTimeSlot() throws ElementNotFoundException, InterruptedException {
        String elementId = TIME_SLOT.getId();
        String elementDescription = TIME_SLOT.name();
        WebElement element = FormFillerUtils.getElementById(elementId, elementDescription, driver);
        FormFillerUtils.selectOptionByIndex(element, elementDescription);
        Thread.sleep(1000);
    }

    private void selectFamilyStatus() throws InterruptedException, ElementNotFoundException {
        String elementId = FAMILY_STATUS.getId();
        String elementDescription = FAMILY_STATUS.name();
        WebElement element = FormFillerUtils.getElementById(elementId, elementDescription, driver);
        FormFillerUtils.selectOption(element, elementDescription, familyStatus);
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

    protected void clickToFirstAvailableDate() throws InterruptedException, ElementNotFoundException, InteractionFailedException, IOException {
        String elementDescription = "DateSelection".toUpperCase();
        String cssSelector = "[data-handler=selectDay]";
        WebElement element = FormFillerUtils.getElementByCssSelector(cssSelector, elementDescription, driver);
        String dateMonth = element.getAttribute("data-month");
        String dateYear = element.getAttribute("data-year");
        String dateDay = element.getText();
        String info = String.format("Selected date: Day:%s Month:%s Year:%s", dateDay,dateMonth, dateYear);
        logger.info(info);
        FormFillerUtils.clickToElement(element, elementDescription);
        Thread.sleep(1);
        FormFillerUtils.saveSourceCodeToFile(driver.getPageSource());
        //FormFillerUtils.saveScreenshot(driver);
    }

    private void clickWeiterButton() throws InterruptedException, ElementNotFoundException, InteractionFailedException, IOException {
        String elementId = "applicationForm:managedForm:proceed";
        String elementDescription = "weiter button".toUpperCase();
        WebElement element = FormFillerUtils.getElementById(elementId, elementDescription, driver);
        FormFillerUtils.clickToElement(element, elementDescription);
        Thread.sleep(3);
        FormFillerUtils.saveSourceCodeToFile(driver.getPageSource());
        FormFillerUtils.saveScreenshot(driver);
    }

    private boolean isResultSuccessful() throws InterruptedException {
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
            } else {
                remainingMinute = 0;
            }
            logger.info(String.format("Element: %s. Process: Getting time. Status: Successfully. Value: %s", elementDescription, timeStr));
        } catch (Exception e) {
            logger.info("Element: {}. Process: Getting time. Status: Failed");
        }
        return remainingMinute;
    }



}
