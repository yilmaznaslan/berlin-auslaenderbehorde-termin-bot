package org.example;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.example.FormFillerUtils.*;
import static org.example.Main.activateRequestId;
import static org.example.Main.getRequestId;

public class FormFiller {
    private final Logger logger = LoggerFactory.getLogger(FormFiller.class);
    private String applicantNumber = "1";
    private String familyStatus = "2";
    private String requestId;
    private String dswid;
    private String dsrid;
    private int searchCount = 0;
    private WebDriver driver;

    public FormFiller(String requestId, String dswid, String dsrid) {
        this.requestId = requestId;
        this.dswid = dswid;
        this.dsrid = dsrid;
    }

    public void fillTheForm() throws InterruptedException {
        while (true) {
            try {
                driver = initDriver();
                goToFormPage(requestId, dswid, dsrid);
                double remainingMinute = getRemainingTime();
                if (remainingMinute <= 1) {
                    logger.warn("Time is up");
                    requestId = getRequestId();
                    activateRequestId(requestId);
                    this.dswid = Main.dswid;
                    this.dsrid = Main.dsrid;
                }
                selectCitizenshipValue();
                setApplicantsNumber(applicantNumber);
                selectFamilyStatus(familyStatus);
                clickToServiceType();
                clickToVisaGroup();
                clickToVisaBlueCard();
                clickNextButton();
                if (isResultSuccessful()) {
                    String url = driver.getCurrentUrl();
                    initDriverWithHead().get(url);
                    String pageSource = driver.getPageSource();
                    writeSourceCodeToFile(pageSource);
                    break;
                }

                this.searchCount = this.searchCount + 1;
                logger.info("Completed search count: {}", searchCount);
                Thread.sleep(100);
            } catch (NullPointerException e) {
                logger.warn("Some error happend. Reason :{}", e.getCause());
            } finally {
                driver.close();
            }

        }

    }

    public void goToFormPage(String requestId, String dswid, String dsrid) {
        String hostUrl = "https://otv.verwalt-berlin.de/ams/TerminBuchen/wizardng";
        String targetUrl = hostUrl + "/" + requestId + "?dswid=" + dswid + "&dsrid=" + dsrid;
        logger.info("Finding the URL: {}", targetUrl);
        driver.get(targetUrl);
    }

    private WebDriver initDriver() {
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
        WebElement element = getElementByName(elementName, "citizenship", driver);
        selectOption(element, "citizenship", "163");
    }

    private void setApplicantsNumber(String applicantNumber) throws InterruptedException {
        String elementName = "personenAnzahl_normal";
        String elementValue = applicantNumber;
        WebElement element = getElementByName(elementName, "appliacntsNumber", driver);
        selectOption(element, "appliacntsNumber", applicantNumber);
    }

    private void selectFamilyStatus(String familyStatus) throws InterruptedException {
        String elementName = "lebnBrMitFmly";
        String elementValue = familyStatus;
        WebElement element = getElementByName(elementName, "FamiliyStatus", driver);
        selectOption(element, "FamiliyStatus", familyStatus);
    }

    private void clickToServiceType() throws InterruptedException {
        String elementName = "service type";
        String elementXPath = "//*[@id=\"xi-div-30\"]/div[1]/label/p";
        WebElement element = getElementByXPath(elementXPath, "serviceType", driver);
        clickToElement(element, elementName);
    }

    private void clickToVisaBlueCard() throws InterruptedException {
        String elementName = "click blue card";
        String elementXpath = "//*[@id=\"inner-163-0-1\"]/div/div[4]/div/div[11]/label";
        WebElement element = getElementByXPath(elementXpath, "blueCardVisa", driver);
        clickToElement(element, elementName);
    }

    private void clickToVisaGroup() throws InterruptedException {
        String elementName = "set visa  group";
        String elementXpath = "//*[@id=\"inner-163-0-1\"]/div/div[3]/label";
        WebElement element = getElementByXPath(elementXpath, "visaGroup", driver);
        clickToElement(element, elementName);
    }

    private void clickNextButton() throws InterruptedException {
        String elementXpath = "//*[@id=\"applicationForm:managedForm:proceed\"]";
        String elementName = "next button";
        WebElement element = getElementByXPath(elementXpath, "clickButton", driver);
        clickToElement(element, elementName);
        Thread.sleep(5000);
    }

    private boolean isResultSuccessful() throws InterruptedException {
        String stageXPath = ".//ul/li[2]/span";
        WebElement element = getElementByXPath(stageXPath, "activeSectionTab", driver);

        String stageText = element.getText();
        if (stageText.contains("Terminauswahl")) {
            logger.info("Found a place !");
            return true;
        }
        return false;
    }

    public double getRemainingTime() throws InterruptedException {
        String elementXpath = "//*[@id=\"progressBar\"]/div";
        WebElement timeBar = getElementByXPath(elementXpath, "remainingTime", driver);
        String timeStr = timeBar.getText();
        int remainingMinute = Integer.parseInt(timeStr.split(":")[0]);
        logger.info("Remaining time: {}", timeStr);
        return remainingMinute;
    }
}
