package com.yilmaznaslan.formhandlers;

import static com.yilmaznaslan.AppointmentFinder.TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS;
import static com.yilmaznaslan.utils.DriverUtils.extractSessionId;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Business Access Layer for getting english landing page
 */
public class Section1MainPageHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(Section1MainPageHandler.class);

    private final RemoteWebDriver driver;

    public Section1MainPageHandler(RemoteWebDriver remoteWebDriver) {
        this.driver = remoteWebDriver;
    }

    public String fillAndSendForm() {
        LOGGER.info("Starting to fill the form");
        clickBookAppointment();
        clickToAcceptConsent();
        sendForm();
        return extractSessionId(driver);
    }

    private void clickBookAppointment() {
        String labelValue = "Book Appointment";
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS));
        wait.until(currentDriver -> {
            try {
                currentDriver.findElement(By.linkText(labelValue)).click();
                return true;
            } catch (Exception e) {
                return false;
            }
        });
    }



    private void clickToAcceptConsent() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS));
        WebElement webElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[type='checkbox']")));
        wait.until(ExpectedConditions.elementToBeClickable(webElement));
        webElement.click();
    }

    private void sendForm() {
        String elementXpath = "//*[@id=\"applicationForm:managedForm:proceed\"]";
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS)).until(ExpectedConditions.presenceOfElementLocated(By.xpath(elementXpath)));
        element.click();
    }

}
