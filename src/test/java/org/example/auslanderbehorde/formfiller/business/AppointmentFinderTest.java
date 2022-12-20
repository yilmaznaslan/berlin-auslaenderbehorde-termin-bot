package org.example.auslanderbehorde.formfiller.business;

import org.example.auslanderbehorde.appointmentfinder.business.AppointmentFinder;
import org.example.auslanderbehorde.formfiller.exceptions.ElementNotFoundException;
import org.example.auslanderbehorde.formfiller.exceptions.InteractionFailedException;
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

class AppointmentFinderTest {

    String path_DE = FormFillerBAL.class.getClassLoader().getResource("org/example/form/business/dateSelection_DE.html").getPath();

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