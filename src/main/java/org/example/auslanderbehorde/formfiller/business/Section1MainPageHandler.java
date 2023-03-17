package org.example.auslanderbehorde.formfiller.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.auslanderbehorde.formfiller.exceptions.ElementNotFoundTimeoutException;
import org.example.auslanderbehorde.formfiller.exceptions.InteractionFailedException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Business Access Layer for getting english landing page
 */
public class Section1MainPageHandler {

    private final Logger logger = LogManager.getLogger(Section1MainPageHandler.class);

    private RemoteWebDriver driver;

    public Section1MainPageHandler(RemoteWebDriver remoteWebDriver) {
        this.driver = remoteWebDriver;
    }

    public void fillAndSendForm() throws ElementNotFoundTimeoutException, InteractionFailedException, InterruptedException {
        logger.info("Starting to fill the form");
        clickBookAppointment();
        clickToAcceptConsent();
        sendForm();
    }

    private void clickBookAppointment() throws InterruptedException, ElementNotFoundTimeoutException, InteractionFailedException {
        String labelValue = "Book Appointment";
        WebElement element = FormFillerUtils.getElementByTextValue(labelValue, labelValue, driver);
        FormFillerUtils.clickToElement(element, labelValue);
    }

    private void clickToAcceptConsent() throws InterruptedException, ElementNotFoundTimeoutException, InteractionFailedException {
        WebElement webElement = FormFillerUtils.getElementByTagName("checkbox", "checkbox", driver);
        FormFillerUtils.clickToElement(webElement, "");
    }

    private void sendForm() throws InterruptedException, ElementNotFoundTimeoutException, InteractionFailedException {
        String elementXpath = "//*[@id=\"applicationForm:managedForm:proceed\"]";
        String elementDescription = "clickButton".toUpperCase();
        WebElement element = FormFillerUtils.getElementByXPath(elementXpath, elementDescription, driver);
        FormFillerUtils.clickToElement(element, elementDescription);
    }

    public RemoteWebDriver getDriver() {
        return this.driver;
    }
}
