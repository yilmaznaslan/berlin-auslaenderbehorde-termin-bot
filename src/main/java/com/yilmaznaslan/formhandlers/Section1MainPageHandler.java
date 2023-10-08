package com.yilmaznaslan.formhandlers;

import com.yilmaznaslan.utils.DriverUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

import static com.yilmaznaslan.TerminFinder.TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS;

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
        getHomePage();
        DriverUtils.waitUntilFinished(driver);
        LOGGER.info("Starting to fill the form");
        clickBookAppointment();
        clickToAcceptConsent();
        sendForm();
        return extractSessionId();
    }

    private String extractSessionId() {
        LOGGER.info("Waiting for the page to be loaded");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.pollingEvery(Duration.ofMillis(100));
        AtomicReference<String> sessionId = new AtomicReference<>();
        wait.until(webDriver -> {
            try {
                String currentUrl = webDriver.getCurrentUrl();
                LOGGER.info("Current URL: {}", currentUrl);
                if (currentUrl.contains("wizardng/")) {
                    LOGGER.info("URL contains wizardng, capturing session id: {}", currentUrl);
                    sessionId.set(currentUrl);
                    return true;
                }
                return false;
            } catch (TimeoutException e) {
                LOGGER.error("Failed to get current URL, will try again");
                return false;
            } catch (Exception exception) {
                LOGGER.error("Failed to get current URL, unknown reason:", exception);
                return false;
            }

        });
        return sessionId.get();
    }

    private void clickBookAppointment() {
        String labelValue = "Book Appointment";
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS));
        wait.until(ddriver -> {
            try {
                driver.findElement(By.linkText(labelValue)).click();
                return true;
            } catch (Exception e) {
                return false;
            }
        });
    }

    private void getHomePage() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info("Starting to {}", methodName);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS));
        wait.until(webDriver -> {
            try {
                String url = "https://otv.verwalt-berlin.de/ams/TerminBuchen?lang=en";
                LOGGER.info(String.format("Getting the URL: %s", url));
                driver.get(url);
                return true;
            } catch (Exception e) {
                LOGGER.error("Getting home page failed. Reason: ", e);
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
