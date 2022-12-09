package org.example.auslanderbehorde;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

class FormFillerUtilsTest {


    @Test
    void ASSERT_THAT_available_date_is_selected() throws ElementNotFoundException, InterruptedException {
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
    void writeSourceCodeToFile() {
        FormFillerUtils.writeSourceCodeToFile("adasd", 4);
    }
}