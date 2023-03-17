package org.example.auslanderbehorde.formfiller.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class DriverManager {
    private final static Logger logger = LogManager.getLogger(TerminFinder.class);

    public static RemoteWebDriver initDriverHeadless() {
        String seleniumDriverHost = System.getenv().getOrDefault("SELENIUM_GRID_HOST", "localhost");
        ChromeOptions options = new ChromeOptions();

        // Add options to make Selenium-driven browser look more like a regular user's browser
        options.addArguments("--disable-blink-features=AutomationControlled"); // Remove "navigator.webdriver" flag
        options.addArguments("--disable-infobars"); // Disable infobars
        options.addArguments("--start-maximized"); // Start the browser maximized
        options.addArguments("--disable-extensions"); // Disable extensions

        String remoteUrl = "http://" + seleniumDriverHost + ":4444/wd/hub";
        try {
            RemoteWebDriver driver = new RemoteWebDriver(new URL(remoteUrl), options);
            logger.info("Driver is initialized.");
            return driver;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

    }

}
