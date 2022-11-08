package org.example.auslanderbehorde;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;

import static org.example.notifications.Twilio.makeCall;
import static org.example.notifications.Twilio.sendSMS;

public class FormFiller extends TimerTask {

    private final Logger logger = LoggerFactory.getLogger(FormFiller.class);
    public static final int FORM_REFRESH_PERIOD_MILISECONDS = 1000;

    private final String citizenshipValue;
    private final String applicantNumber;
    private final String familyStatus;
    private final String visaType;

    private String requestId;
    private String dswid;
    private String dsrid;
    private int searchCount = 0;

    private final WebDriver driver;
    private Timer timer = new Timer(true);


    public void startScanning(){
        timer.scheduleAtFixedRate(this, 0, FORM_REFRESH_PERIOD_MILISECONDS);
    }

    public FormFiller(String requestId, String dswid, String dsrid, FormInputs formInputs) {
        this.requestId = requestId;
        this.dswid = dswid;
        this.dsrid = dsrid;
        this.driver = initDriverHeadless();

        this.citizenshipValue = formInputs.getCitizenshipValue();
        this.applicantNumber = formInputs.getApplicationsNumber();
        this.familyStatus = formInputs.getFamilyStatus();
        this.visaType = formInputs.getVisaType();
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
            selectApplicantsNumber();
            selectFamilyStatus();
            clickServiceType();
            clickVisaGroup();
            clickVisaBlueCard();
            clickNextButton();
            if (isResultSuccessful()) {
                logger.info("Found a place !");
                String url = driver.getCurrentUrl();
                logger.info("URL: {}", url);
                initDriverWithHead().get(url);
                String pageSource = driver.getPageSource();
                FormFillerUtils.writeSourceCodeToFile(pageSource);
                String myPhoneNumber = System.getenv("myPhoneNumber");
                makeCall(myPhoneNumber);
                sendSMS(myPhoneNumber, url);
                clickToActiveDate();
                selectTime();
                clickWeiterButton();
                //timer.cancel();
            }
            this.searchCount = this.searchCount + 1;
            logger.info("Completed search count: {}", searchCount);

        } catch (Exception e) {
            logger.warn("Some error occured. Reason ", e.getCause());
        }

    }

    public void goToFormPage(String requestId, String dswid, String dsrid) {
        String hostUrl = "https://otv.verwalt-berlin.de/ams/TerminBuchen/wizardng";
        String targetUrl = hostUrl + "/" + requestId + "?dswid=" + dswid + "&dsrid=" + dsrid;
        logger.info("Finding the URL: {}", targetUrl);
        driver.get(targetUrl);
    }

    private WebDriver initDriverHeadless() {
        logger.info("Initializing driver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-logging");
        options.addArguments("--headless");
        return new ChromeDriver(options);
    }

    private WebDriver initDriverWithHead() {
        logger.info("Initializing driver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-logging");
        return new ChromeDriver(options);
    }

    private void selectCitizenshipValue() throws InterruptedException {
        String elementName = "sel_staat";
        String elementDescription = "Citizenship".toUpperCase();
        WebElement element = FormFillerUtils.getElementByName(elementName, elementDescription, driver);
        FormFillerUtils.selectOption(element, "citizenship", citizenshipValue);
    }

    private void selectApplicantsNumber() throws InterruptedException {
        String elementName = "personenAnzahl_normal";
        String elementDescription = "appliacntsNumber".toUpperCase();
        WebElement element = FormFillerUtils.getElementByName(elementName, elementDescription, driver);
        FormFillerUtils.selectOption(element, applicantNumber, applicantNumber);
    }

    private void selectFamilyStatus() throws InterruptedException {
        String elementName = "lebnBrMitFmly";
        String elementDescription = "FamilyStatus".toUpperCase();
        WebElement element = FormFillerUtils.getElementByName(elementName, elementDescription, driver);
        FormFillerUtils.selectOption(element, elementDescription, familyStatus);
    }

    private void clickServiceType() throws InterruptedException {
        String elementXPath = "//*[@id=\"xi-div-30\"]/div[1]/label/p";
        String elementDescription = "serviceType".toUpperCase();
        WebElement element = FormFillerUtils.getElementByXPath(elementXPath, elementDescription, driver);
        FormFillerUtils.clickToElement(element, elementDescription);
    }

    private void clickVisaGroup() throws InterruptedException {
        String elementXpath = "//*[@id=\"inner-163-0-1\"]/div/div[3]/label";
        String elementDescription = "visaGroup".toUpperCase();
        WebElement element = FormFillerUtils.getElementByXPath(elementXpath, elementDescription, driver);
        FormFillerUtils.clickToElement(element, elementDescription);
    }

    private void clickVisaBlueCard() throws InterruptedException {
        String elementXpath = "//*[@id=\"inner-163-0-1\"]/div/div[4]/div/div[11]/label";
        String elementDescription = "blueCardVisa".toUpperCase();
        WebElement element = FormFillerUtils.getElementByXPath(elementXpath, elementDescription , driver);
        FormFillerUtils.clickToElement(element, elementDescription);
    }

    private void clickNextButton() throws InterruptedException {
        String elementXpath = "//*[@id=\"applicationForm:managedForm:proceed\"]";
        String elementDescription = "clickButton".toUpperCase();
        WebElement element = FormFillerUtils.getElementByXPath(elementXpath, elementDescription , driver);
        FormFillerUtils.clickToElement(element, elementDescription);
        Thread.sleep(5000);
    }

    private void clickToActiveDate() throws InterruptedException {
        String elementDescription = "DateSelection".toUpperCase();
        String elementXpath = "//*[@id=\"xi-div-2\"]/div/div[1]/table/tbody/tr[5]/td[1]/a";
        WebElement element = FormFillerUtils.getElementByXPath(elementXpath, elementDescription, driver);
        FormFillerUtils.clickToElement(element, elementDescription);
    }

    private void selectTime() throws InterruptedException {
        String elementName = "dd_zeiten";
        String elementDescription = "Select time".toUpperCase();
        WebElement element = FormFillerUtils.getElementByName(elementName, elementDescription, driver);
        FormFillerUtils.selectOptionByIndex(element, elementDescription);
    }

    private void clickWeiterButton() throws InterruptedException {
        String elementXpath = "//*[@id=\"applicationForm:managedForm:proceed\"]";
        String elementDescription = "weiter button".toUpperCase();
        WebElement element = FormFillerUtils.getElementByXPath(elementXpath, elementDescription, driver);
        FormFillerUtils.clickToElement(element, elementDescription);
    }

    private boolean isResultSuccessful() throws InterruptedException {
        String stageXPath = ".//ul/li[2]/span";
        WebElement element = FormFillerUtils.getElementByXPath(stageXPath, "activeSectionTab", driver);

        String stageText = element.getText();
        return stageText.contains("Terminauswahl");

    }

    private double getRemainingTime() throws InterruptedException {
        String elementXpath = "//*[@id=\"progressBar\"]/div";
        WebElement timeBar = FormFillerUtils.getElementByXPath(elementXpath, "remainingTime", driver);
        String timeStr = timeBar.getText();
        int remainingMinute;
        if (timeStr != null){
            remainingMinute = Integer.parseInt(timeStr.split(":")[0]);
        } else{
            remainingMinute = 0;
        }
        logger.info("Remaining time: {}", timeStr);
        return remainingMinute;
    }


}
