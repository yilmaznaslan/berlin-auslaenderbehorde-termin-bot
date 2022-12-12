package org.example.auslanderbehorde;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.*;

class FormFillerTest {

    void testToClick(){
        // GIVEN
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);
        String url = "file:/Users/yilmaznaci.aslan/repositories/berlinTerminFinder/src/test/resources/TerminbuchenServicewahl.html";


        // WHEN
        driver.get(url);

    }

}