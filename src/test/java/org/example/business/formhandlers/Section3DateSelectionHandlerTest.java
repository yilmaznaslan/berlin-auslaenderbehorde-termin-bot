package org.example.business.formhandlers;

import org.example.exceptions.ElementNotFoundTimeoutException;
import org.example.utils.FormFillerUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

class Section3DateSelectionHandlerTest {

    static String path = Section4VisaFormHandler.class.getClassLoader().getResource("page_section3_dateSelection_2023-02-03_10:23:27.html").getPath();
    static String url = "file:".concat(path);

    static ChromeDriver driver;

    Section3DateSelectionHandler formFiller = new Section3DateSelectionHandler(driver);


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
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
    }

    @AfterAll
    static void quitDriver() {
        driver.quit();
    }


    @Test
    void ASSERT_THAT_isCalenderFound_returns_true() {

        // GIVEN
        driver.get(url);

        // WHEN
        boolean actualResult = formFiller.isCalenderFound();

        // THEN
        Assertions.assertTrue(actualResult);
    }

    @Test
    void ASSERT_THAT_isDateVerified_returns_true() throws ElementNotFoundTimeoutException, InterruptedException {
        // GIVEN
        driver.get(url);
        String cssSelector = "[data-handler=selectDay]";
        FormFillerUtils.saveSourceCodeToFile(driver.getPageSource(), this.getClass().getSimpleName(), "handling_date");
        FormFillerUtils.saveScreenshot(driver, this.getClass().getSimpleName(), "handling_Date");
        WebElement element = FormFillerUtils.getElementByCssSelector(cssSelector, "elementDescription", driver);

        // WHEN
        boolean actualResult = formFiller.isDateVerified(element);

        // THEN
        Assertions.assertTrue(actualResult);
    }
}