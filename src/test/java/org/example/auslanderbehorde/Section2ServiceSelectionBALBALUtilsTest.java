package org.example.auslanderbehorde;

import org.example.auslanderbehorde.formfiller.exceptions.ElementNotFoundTimeoutException;
import org.example.auslanderbehorde.formfiller.business.FormFillerUtils;
import org.example.auslanderbehorde.formfiller.enums.applyforavisa.EconomicActivityVisaDe;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;

class Section2ServiceSelectionBALBALUtilsTest {

    String path_EN = Section2ServiceSelectionBALBALUtilsTest.class.getClassLoader().getResource("org/example/TerminbuchenServicewahl.html").getPath();
    String path_DE = Section2ServiceSelectionBALBALUtilsTest.class.getClassLoader().getResource("org/example/TerminbuchenServicewahl_DE.html").getPath();

    String url_EN = "file:".concat(path_EN);
    String url_DE = "file:".concat(path_DE);

    WebDriver driver;

    @BeforeEach
    void initDriver(){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        this.driver = new ChromeDriver(options);
    }

    @Test
    void ASSERT_THAT_available_date_is_selected() throws ElementNotFoundTimeoutException, InterruptedException {
        // GIVEN
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);
        String url = "file:/Users/yilmaznaci.aslan/repositories/berlinTerminFinder/src/test/resources/terminDateSelect.html";
        String expectedMonth = "11";
        String expectedYear  = "2022";
        String expectedDay = "5";

        // WHEN
        driver.get(url);
        WebElement activeDateElement = FormFillerUtils.getElementByCssSelector("[data-handler=selectDay]", "Date selection",driver);
        String actualMonth = activeDateElement.getAttribute("data-month");
        String actualYear = activeDateElement.getAttribute("data-year");
        String actualDay = activeDateElement.getText();


        // THEN
        Assertions.assertEquals(expectedDay, actualDay);
        Assertions.assertEquals(expectedYear,actualYear);
        Assertions.assertEquals(expectedMonth, actualMonth);
    }

    @Test
    void AASERT_THAT_screenshot_is_saved_WHEN_testSaveScreenshot_is_called() throws IOException {
        // GIVEN

        // WHEN
        driver.get(url_EN);


        FormFillerUtils.saveScreenshot(driver, "");

        // THEN
    }

    @Test
    void ASSERT_THAT_element_is_returned_WHEN_getById_is_called_GIVEN_THAT_current_page_is_servicewahl() throws ElementNotFoundTimeoutException, InterruptedException {
        // GIVEN
        String id = "SERVICEWAHL_DE323-0-1-3-328338";

        // WHEN
        driver.get(url_EN);
        WebElement element = FormFillerUtils.getElementById(id, "", driver);

        // THEN
        String label = "Aufenthaltserlaubnis für eine Berufsausbildung (§ 16a)";
        Assertions.assertEquals(label, element.getAttribute("data-tag0"));
    }

    @Test
    void ASSERT_THAT_element_is_returned_WHEN_getById_is_called_GIVEN_THAT_current_page_is_servicewahl_DE() throws ElementNotFoundTimeoutException, InterruptedException {
        // GIVEN
        String id = EconomicActivityVisaDe.BLUECARD.getId();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        WebDriver asd =  new ChromeDriver(options);
        // WHEN
        asd.get(url_DE);
        //WebElement  element = asd.findElement(By.id(id));
        WebElement element = FormFillerUtils.getElementById(id, "", asd);

        // THEN
        String label = EconomicActivityVisaDe.BLUECARD.getDataTag0();
        Assertions.assertEquals(label, element.getAttribute("data-tag0"));
    }
}