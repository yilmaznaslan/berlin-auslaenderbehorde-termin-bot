package org.example.utils;


import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

public class DriverUtils {
    private static final Logger logger = LoggerFactory.getLogger(DriverUtils.class);
    public static final int TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS = 25;
    public static final int TIMEOUT_FOR_GETTING_CALENDER_ELEMENT_IN_SECONDS = 5;

    public static final int TIMEOUT_FOR_INTERACTING_IN_SECONDS = 25;

    private DriverUtils() {
    }

    public static RemoteWebDriver initDriver() {
        String seleniumDriverHost = System.getenv().getOrDefault("SELENIUM_GRID_HOST", "localhost");
        String remoteUrl = "http://" + seleniumDriverHost + ":4444/wd/hub";
        try {
            RemoteWebDriver driver = new RemoteWebDriver(new URL(remoteUrl), getChromeOptions());
            driver.manage().window().maximize();
            logger.info("Driver is initialized.");
            return driver;
        } catch (MalformedURLException e) {
            logger.info(String.format("The url of the selenium host: %s is malformed.Reason: ", remoteUrl), e);
            return null;
        }

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
