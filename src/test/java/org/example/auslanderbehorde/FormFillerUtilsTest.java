package org.example.auslanderbehorde;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

class FormFillerUtilsTest {


    @Test
    void ASSERT_THAT_date_is_selected() throws ElementNotFoundException, InterruptedException {
        // GIVEN
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);
        String url = "file:/Users/yilmaznaci.aslan/repositories/berlinTerminFinder/src/test/resources/terminDateSelect.html";

        // WHEN
        driver.get(url);
        List<WebElement> asd = FormFillerUtils.getElementsByTagName("td", driver);

        // THEN
        Assertions.assertEquals(77, asd.size());
    }


}