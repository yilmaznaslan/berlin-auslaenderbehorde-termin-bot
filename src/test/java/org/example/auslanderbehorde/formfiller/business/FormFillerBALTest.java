package org.example.auslanderbehorde.formfiller.business;

import org.example.auslanderbehorde.formfiller.enums.EconomicActivityVisaDeEnum;
import org.example.auslanderbehorde.formfiller.exceptions.InteractionFailedException;
import org.example.auslanderbehorde.formfiller.model.FormInputs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

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
        //options.addArguments("--headless");
        this.driver = new ChromeDriver(options);
        //this.underTest = new FormFillerBAL(formInputs, driver);
    }

    @Test
    void ASSERT_THAT_form_successfully_filled_WHEN_run_is_called() {
        // GIVEN

        // WHEN
        FormFillerBAL formFillerBAL = new FormFillerBAL(new FormInputs("163", "1", "2", EconomicActivityVisaDeEnum.BLUECARD));
        formFillerBAL.startScanning();
        // THEN

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

    @Test
    void ASSERT_THAT_appointmentSelection_is_not_captured_WHEN_isAppointmentSelectionPageOpened_is_called() {
        // GIVEN
        String path = FormFillerBAL.class.getClassLoader().getResource("page_dateSelection2022-12-23_08:11:45.html").getPath();
        String url = "file:".concat(path);

        // WHEN
        driver.get(url);
        underTest = new FormFillerBAL(formInputs);
        boolean actualResult;
        try {
            actualResult = underTest.isCalenderOpened();
        } catch (InteractionFailedException e) {
            throw new RuntimeException(e);
        }

        // THEN
        Assertions.assertFalse(actualResult);
    }

    @Test
    void ASSERT_THAT_appointmentSelection_is_captured_WHEN_isAppointmentSelectionPageOpened_is_called_GIVEN_termin_page_is_correct() {
        // GIVEN

        // WHEN
        driver.get(url_DE);
        underTest = new FormFillerBAL(formInputs);
        boolean actualResult;
        try {
            actualResult = underTest.isCalenderOpened();
        } catch (InteractionFailedException e) {
            throw new RuntimeException(e);
        }

        // THEN
        Assertions.assertTrue(actualResult);
    }

}