package org.example;

import org.example.utils.IoUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.example.utils.DriverUtils.getChromeOptions;

public class BaseTestSetup {

    public static ChromeDriver driver;

    @BeforeAll
    static void initDriver() {
        ChromeOptions chromeOptions = getChromeOptions();
        chromeOptions.addArguments("--headless");
        driver = new ChromeDriver(chromeOptions);
        driver.manage().window().maximize();
        IoUtils.isS3Enabled = false;
        IoUtils.isLocalSaveEnabled = false;
    }

    @AfterAll
    static void quitDriver() {
        driver.quit();
    }

}