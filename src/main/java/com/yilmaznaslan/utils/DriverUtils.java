package com.yilmaznaslan.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.yilmaznaslan.AppointmentFinder.TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS;

public class DriverUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(DriverUtils.class);

    private DriverUtils() {
    }

    public static RemoteWebDriver initDriver() {
        String seleniumDriverHost = System.getenv().getOrDefault("SELENIUM_GRID_HOST", "localhost");
        String remoteUrl = "http://" + seleniumDriverHost + ":4444/wd/hub";
        LOGGER.info("Initializing the driver. Host: {}", seleniumDriverHost);
        RemoteWebDriver driver = null;
        try {
            driver = new RemoteWebDriver(new URL(remoteUrl), getChromeOptions());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        driver.manage().window().maximize();
        LOGGER.info("Driver is initialized.");
        return driver;

    }

    public static void waitUntilFinished(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS));
        wait.pollingEvery(Duration.ofSeconds(1));

        wait.until(webDriver -> {
            try {
                JavascriptExecutor javascriptExecutor = (JavascriptExecutor) webDriver;
                boolean result = (Boolean) javascriptExecutor.executeScript("return jQuery.active == 0");
                LOGGER.info("jQuery.active: {}", result);
                return result;
            } catch (UnhandledAlertException e) {
                LOGGER.error("UnhandledAlertException occurred during waiting until page is loaded.Clicking", e);
                Alert alert = driver.switchTo().alert();
                alert.accept();
                return false;
            } catch (JavascriptException javascriptException) {
                LOGGER.warn("Failed to run javascript.");
                return false;
            } catch (Exception e) {
                LOGGER.warn("An exception occurred while waiting for the page to be loaded. Reason", e);
                return false;
            }
        });

    }

    public static ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();

        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");

        // Add options to make Selenium-driven browser look more like a regular user's browser
        options.addArguments("--disable-blink-features=AutomationControlled"); // Remove "navigator.webdriver" flag
        options.addArguments("--disable-infobars"); // Disable infobars
        options.addArguments("--start-maximized"); // Start the browser maximized
        options.addArguments("--disable-extensions"); // Disable extensions

        // Add a fake user-agent to make it look like a regular browser
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3");

        options.setCapability("unexpectedAlertBehavior", "accept");

        Map<String, Object> cloudOptions = new HashMap<>();
        cloudOptions.put("unexpectedAlertBehavior", "accept");

        //options.setCapability("cloud:options", cloudOptions);


        return options;
    }

    public static String extractSessionId(WebDriver driver) {
        LOGGER.info("Extracting the session Url");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS));
        wait.pollingEvery(Duration.ofMillis(50));
        AtomicReference<String> sessionId = new AtomicReference<>();
        try {
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
        } catch (TimeoutException e) {
            LOGGER.error("Failed to get current URL, will try again");
        } catch (Exception exception) {
            LOGGER.error("Failed to get current URL, unknown reason:", exception);
        }

        return sessionId.get();
    }

    private void waitUntilPageIsLoaded(RemoteWebDriver driver) {
        LOGGER.info("Waiting for the page to be loaded");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        AtomicBoolean isUrlCaptured = new AtomicBoolean(false);
        AtomicReference<String> urlAfterSendForm = new AtomicReference<>();
        wait.until(webDriver -> {
            String currentUrl = webDriver.getCurrentUrl();
            LOGGER.debug("Current URL: {}", currentUrl);

            if (!isUrlCaptured.get()) {
                isUrlCaptured.set(currentUrl.contains("dswid") && currentUrl.contains("dsrid") && currentUrl.contains(
                        "wizardng"));
            } else if (urlAfterSendForm.get() == null) {
                urlAfterSendForm.set(currentUrl);
                LOGGER.info("URL after send form: {}", urlAfterSendForm);
            }
            return isUrlCaptured.get() && currentUrl.equals("https://otv.verwalt-berlin.de/ams/TerminBuchen");

        });
        LOGGER.info("Page is loaded");
    }

}
