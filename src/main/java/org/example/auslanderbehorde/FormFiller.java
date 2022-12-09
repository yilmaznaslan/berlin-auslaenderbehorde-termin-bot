package org.example.auslanderbehorde;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static org.example.auslanderbehorde.FormFillerUtils.SLEEP_DURATION_IN_MILISECONDS;
import static org.example.auslanderbehorde.FormFillerUtils.TIMEOUT_FOR_INTERACTING_IN_SECONDS;
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
                File scrFile1 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(scrFile1, new File("/Users/yilmaznaci.aslan/repositories/berlinTerminFinder/screenshot11.png"));

                logger.info("URL: {}", url);
                initDriverWithHead().get(url);
                String pageSource = driver.getPageSource();
                FormFillerUtils.writeSourceCodeToFile(pageSource);
                String myPhoneNumber = System.getenv("myPhoneNumber");
                makeCall(myPhoneNumber);
                sendSMS(myPhoneNumber, url);
                clickToFirstAvailableDate();
                //selectTime();
                clickWeiterButton();
                //timer.cancel();
            }

            this.searchCount = this.searchCount + 1;
            logger.info("Completed search count: {}", searchCount);

        } catch (Exception e) {
            logger.warn("Some error occurred. Reason ", e);
            this.driver.quit();
            this.driver = initDriverHeadless();
        }

    }

    public void goToFormPage(String requestId, String dswid, String dsrid) {
        String hostUrl = "https://otv.verwalt-berlin.de/ams/TerminBuchen/wizardng";
        String targetUrl = hostUrl + "/" + requestId + "?dswid=" + dswid + "&dsrid=" + dsrid;
        logger.info("Finding the URL: {}", targetUrl);
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
        String elementName = "sel_staat";
        String elementDescription = "Citizenship".toUpperCase();
        WebElement element = FormFillerUtils.getElementByName(elementName, elementDescription, driver);
        FormFillerUtils.selectOption(element, elementDescription, citizenshipValue);
    }

    private void selectApplicantsNumber() throws InterruptedException, ElementNotFoundException {
        String elementName = "personenAnzahl_normal";
        String elementDescription = "appliacntsNumber".toUpperCase();
        WebElement element = FormFillerUtils.getElementByName(elementName, elementDescription, driver);
        FormFillerUtils.selectOption(element, elementDescription, applicantNumber);
    }

    private void selectFamilyStatus() throws InterruptedException, ElementNotFoundException {
        String elementName = "lebnBrMitFmly";
        String elementDescription = "FamilyStatus".toUpperCase();
        WebElement element = FormFillerUtils.getElementByName(elementName, elementDescription, driver);
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

    private void clickVisaBlueCard() throws InterruptedException, ElementNotFoundException, InteractionFailedException {
        String elementXpath = "//*[@id=\"inner-163-0-1\"]/div/div[4]/div/div[11]/label";
        String elementDescription = "blueCardVisa".toUpperCase();
        WebElement element = FormFillerUtils.getElementByXPath(elementXpath, elementDescription, driver);
        FormFillerUtils.clickToElement(element, elementDescription);
    }

    private void clickNextButton() throws InterruptedException, ElementNotFoundException, InteractionFailedException {
        String elementXpath = "//*[@id=\"applicationForm:managedForm:proceed\"]";
        String elementDescription = "clickButton".toUpperCase();
        WebElement element = FormFillerUtils.getElementByXPath(elementXpath, elementDescription, driver);
        FormFillerUtils.clickToElement(element, elementDescription);
    }

    protected void clickToFirstAvailableDate() throws InterruptedException, ElementNotFoundException, InteractionFailedException {
        String elementDescription = "DateSelection".toUpperCase();
        String cssSelector = "[data-handler=selectDay]";
        WebElement element = FormFillerUtils.getElementByCssSelector(cssSelector, elementDescription, driver);
        FormFillerUtils.clickToElement(element, elementDescription);
    }
    private void clickWeiterButton() throws InterruptedException, ElementNotFoundException, InteractionFailedException {
        String elementXpath = "//*[@id=\"applicationForm:managedForm:proceed\"]";
        String elementDescription = "weiter button".toUpperCase();
        WebElement element = FormFillerUtils.getElementByXPath(elementXpath, elementDescription, driver);
        FormFillerUtils.clickToElement(element, elementDescription);
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
                logger.info("Element: {}. Process: Getting Text. Status: Successfully. Value: {}", elementDescription, stageText);
                Thread.sleep(SLEEP_DURATION_IN_MILISECONDS);
                break;
            } catch (Exception e) {
                logger.info("Element: {}. Process: Getting Text. Status: Failed", elementDescription);
            }
            i++;
        }
        if (i > TIMEOUT_FOR_INTERACTING_IN_SECONDS) {
            logger.warn("Element: {}. Process: reading text, Result: Failed Reason: Couldn't select within timeout", elementDescription);
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
            logger.info("Element: {}. Process: Getting time. Status: Successfully. Value: {}", elementDescription, timeStr);
        } catch (Exception e) {
            logger.info("Element: {}. Process: Getting time. Status: Failed");
        }
        return remainingMinute;
    }


}
