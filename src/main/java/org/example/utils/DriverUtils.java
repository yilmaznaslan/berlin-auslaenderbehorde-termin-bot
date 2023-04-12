package org.example.utils;


import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.example.Config.TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS;

public class DriverUtils {
    public static final int TIMEOUT_FOR_GETTING_CALENDER_ELEMENT_IN_SECONDS = 5;
    private static final Logger logger = LoggerFactory.getLogger(DriverUtils.class);

    private DriverUtils() {
    }

    public static RemoteWebDriver initDriver(){
        String seleniumDriverHost = System.getenv().getOrDefault("SELENIUM_GRID_HOST", "localhost");
        String remoteUrl = "http://" + seleniumDriverHost + ":4444/wd/hub";
        RemoteWebDriver driver = null;
        int i = 0;
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        while (i < TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS) {
            try {
                logger.info("Initializing the driver. Host: {}, try: {}", seleniumDriverHost, i);
                driver = new RemoteWebDriver(new URL(remoteUrl), getChromeOptions());
                driver.manage().window().maximize();
                logger.info("Driver is initialized.");
                break;
            } catch (Exception e) {
                logger.error("Failed to initialize the driver. Reason: ", e);
                try {
                    executor.schedule(() -> {
                    }, 1, TimeUnit.SECONDS).get();
                } catch (InterruptedException | ExecutionException ex) {
                    logger.error("Failed to wait for 1 second. Reason: ", ex);
                }
                i++;
            }
        }
        executor.shutdownNow();
        return driver;
    }

    public static void resetDriverGracefully(RemoteWebDriver driver) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.info("Starting to {}", methodName);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS));
        wait.until(webDriver -> {
            try {
                webDriver.close();
                webDriver.quit();
                logger.info("Successfully reset the driver");
                return true;
            } catch (Exception e) {
                logger.error("Failed to reset the driver. Reason: ", e);
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

        return options;
    }

}
