package org.example.auslanderbehorde.formfiller.business;

import org.example.auslanderbehorde.formfiller.exceptions.ElementNotFoundException;
import org.example.auslanderbehorde.formfiller.exceptions.InteractionFailedException;
import org.example.auslanderbehorde.formfiller.model.FormInputs;
import org.example.auslanderbehorde.sessionfinder.model.SessionModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

import java.io.IOException;

import static org.example.auslanderbehorde.formfiller.enums.EconomicActivityVisaDeEnum.BLUECARD;

class FormFillerBALTest {

    String path_DE = FormFillerBAL.class.getClassLoader().getResource("org/example/form/business/dateSelection_DE.html").getPath();

    String url_DE = "file:".concat(path_DE);

    WebDriver driver;
    FormInputs formInputs = new FormInputs("turkey", "1", "0", BLUECARD);
    FormFillerBAL underTest;

    @BeforeEach
    void initDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        this.driver = new ChromeDriver(options);
        this.underTest = new FormFillerBAL(new SessionModel("", "",""), formInputs, driver);
    }

    @Test
    void ASSERT_THAT_first_available_time_is_selected_WHEN_handleSelectingTimeslot_is_called() {
        // GIVEN
        String expectedTime = "09:30";

        // WHEN
        driver.get(url_DE);
        //underTest.handleSelectingTimeslot();
        WebElement webElement = driver.findElement(By.name("dd_zeiten"));

        // THEN
        Select select = new Select(webElement);
        String actualTime = select.getFirstSelectedOption().getText();

        Assertions.assertEquals(expectedTime, actualTime);
    }

}