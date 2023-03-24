package org.example.formhandlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.exceptions.ElementNotFoundTimeoutException;
import org.example.exceptions.InteractionFailedException;
import org.example.utils.DriverUtils;
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
        WebElement element = DriverUtils.getElementByTextValue(labelValue, labelValue, driver);
        DriverUtils.clickToElement(element, labelValue);
    }

    private void clickToAcceptConsent() throws InterruptedException, ElementNotFoundTimeoutException, InteractionFailedException {
        WebElement webElement = DriverUtils.getElementByTagName("checkbox", "checkbox", driver);
        DriverUtils.clickToElement(webElement, "");
    }

    private void sendForm() throws InterruptedException, ElementNotFoundTimeoutException, InteractionFailedException {
        String elementXpath = "//*[@id=\"applicationForm:managedForm:proceed\"]";
        String elementDescription = "clickButton".toUpperCase();
        WebElement element = DriverUtils.getElementByXPath(elementXpath, elementDescription, driver);
        DriverUtils.clickToElement(element, elementDescription);
    }

    public RemoteWebDriver getDriver() {
        return this.driver;
    }
}
