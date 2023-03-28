package org.example.formhandlers;

import com.google.common.annotations.VisibleForTesting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.exceptions.ElementNotFoundTimeoutException;
import org.example.exceptions.InteractionFailedException;
import org.example.utils.DriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.example.utils.DriverUtils.TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS;

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

    @VisibleForTesting
    protected void clickBookAppointment(){
        String labelValue = "Book Appointment";
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS))
                .until(ExpectedConditions.elementToBeClickable(By.linkText(labelValue)));
        element.click();
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
