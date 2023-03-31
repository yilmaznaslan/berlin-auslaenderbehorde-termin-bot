package org.example.formhandlers;

import com.google.common.annotations.VisibleForTesting;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

import static org.example.utils.DriverUtils.TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS;

/**
 * Business Access Layer for getting english landing page
 */
public class Section1MainPageHandler {

    private final Logger logger = LoggerFactory.getLogger(Section1MainPageHandler.class);

    private final RemoteWebDriver driver;

    public Section1MainPageHandler(RemoteWebDriver remoteWebDriver) {
        this.driver = remoteWebDriver;
    }

    public void fillAndSendForm() {
        logger.info("Starting to fill the form");
        clickBookAppointment();
        clickToAcceptConsent();
        sendForm();
    }

    @VisibleForTesting
    protected void clickBookAppointment() {
        String labelValue = "Book Appointment";
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS))
                .until(ExpectedConditions.elementToBeClickable(By.linkText(labelValue)));
        element.click();
    }

    private void clickToAcceptConsent() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS));
        WebElement webElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[type='checkbox']")));
        webElement.click();
    }

    private void sendForm() {
        String elementXpath = "//*[@id=\"applicationForm:managedForm:proceed\"]";
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath(elementXpath)));
        element.click();
    }

    public RemoteWebDriver getDriver() {
        return this.driver;
    }
}
