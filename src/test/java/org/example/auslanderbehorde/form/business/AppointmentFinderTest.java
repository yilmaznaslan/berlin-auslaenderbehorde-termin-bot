package org.example.auslanderbehorde.form.business;

import org.example.auslanderbehorde.form.exceptions.ElementNotFoundException;
import org.example.auslanderbehorde.form.exceptions.InteractionFailedException;
import org.example.auslanderbehorde.form.model.FormInputs;
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

import static org.example.auslanderbehorde.form.enums.EconomicActivityVisaDeEnum.BLUECARD;
import static org.junit.jupiter.api.Assertions.*;

class AppointmentFinderTest {

    String path_DE = FormFiller.class.getClassLoader().getResource("org/example/form/business/dateSelection_DE.html").getPath();

    String url_DE = "file:".concat(path_DE);

    WebDriver driver;
    AppointmentFinder underTest;

    @BeforeEach
    void initDriver() {
        ChromeOptions options = new ChromeOptions();
        //options.addArguments("--headless");
        this.driver = new ChromeDriver(options);
        this.underTest = new AppointmentFinder(driver);
    }

    @Test
    void ASSERT_THAT_first_available_time_is_selected_WHEN_handleSelectingTimeslot_is_called() throws ElementNotFoundException, InterruptedException, InteractionFailedException, IOException {
        // GIVEN
        String expectedTime = "09:30";
        driver.get(url_DE);

        // WHEN
        underTest.handleSelectingTimeslot();
        WebElement webElement = driver.findElement(By.name("dd_zeiten"));

        // THEN
        Select select = new Select(webElement);
        String actualTime = select.getFirstSelectedOption().getText();

        Assertions.assertEquals(expectedTime, actualTime);
    }
}