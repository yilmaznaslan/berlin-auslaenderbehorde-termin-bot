package org.example.auslanderbehorde;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.*;

class FormFillerTest {


    @Test
    void clickToAsd() throws ElementNotFoundException, InterruptedException {
        // GIVEN
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);
        String url = "file:/Users/yilmaznaci.aslan/repositories/berlinTerminFinder/src/test/resources/terminDateSelect.html";

//*[@id="xi-div-2"]/div/div[1]/table/tbody/tr[2]/td[1]/a
        // WHEN
        driver.get(url);
        FormInputs formInputs = new FormInputs("","","","");
        //FormFiller asd = new FormFiller("","","", formInputs, driver);
        //asd.selectTime();
    }

}