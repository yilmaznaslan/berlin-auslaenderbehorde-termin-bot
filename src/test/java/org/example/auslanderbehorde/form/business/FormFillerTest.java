package org.example.auslanderbehorde.form.business;

import org.example.auslanderbehorde.ElementNotFoundException;
import org.example.auslanderbehorde.form.FormInputs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

import static org.example.auslanderbehorde.form.enums.EconomicActivityVisaDeEnum.BLUECARD;

class FormFillerTest {

    String path_DE = FormFiller.class.getClassLoader().getResource("org/example/form/business/dateSelection_DE.html").getPath();

    String url_DE = "file:".concat(path_DE);

    WebDriver driver;
    FormInputs formInputs = new FormInputs("turkey", "1", "0", BLUECARD);
    FormFiller underTest;

    @BeforeEach
    void initDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        this.driver = new ChromeDriver(options);
        this.underTest = new FormFiller("", "", "", formInputs, driver);
    }

    @Test
    void ASSERT_THAT_first_available_time_is_selected_WHEN_setTime_is_called() throws ElementNotFoundException, InterruptedException {
        // GIVEN
        String expectedTime = "09:30";

        // WHEN
        driver.get(url_DE);
        underTest.selectFirstAvailableTimeSlot();
        WebElement webElement = driver.findElement(By.name("dd_zeiten"));

        // THEN
        Select select = new Select(webElement);
        String actualTime = select.getOptions().get(0).getText();

        Assertions.assertEquals(expectedTime, actualTime);
    }

}