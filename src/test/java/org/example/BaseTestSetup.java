package org.example;

import org.example.utils.IoUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class BaseTestSetup {

    public static ChromeDriver driver;

    @BeforeAll
    static void initDriver() {
        ChromeOptions options = new ChromeOptions();
        // Add options to make Selenium-driven browser look more like a regular user's browser
        options.addArguments("--disable-blink-features=AutomationControlled"); // Remove "navigator.webdriver" flag
        options.addArguments("--disable-infobars"); // Disable infobars
        options.addArguments("--start-maximized"); // Start the browser maximized
        options.addArguments("--disable-extensions"); // Disable extensions
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-dev-shm-usage");
        //options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        IoUtils.isS3Enabled = false;
    }

    @AfterAll
    static void quitDriver() {
        driver.quit();
    }

}