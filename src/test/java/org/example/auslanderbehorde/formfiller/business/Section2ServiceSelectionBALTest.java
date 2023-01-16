package org.example.auslanderbehorde.formfiller.business;

import org.example.auslanderbehorde.formfiller.exceptions.ElementNotFoundTimeoutException;
import org.example.auslanderbehorde.formfiller.exceptions.InteractionFailedException;
import org.example.auslanderbehorde.formfiller.model.FormInputs;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

import static org.example.auslanderbehorde.formfiller.enums.EconomicActivityVisaDeEnum.BLUECARD;

class Section2ServiceSelectionBALTest {

    //String path_DE = Section2ServiceSelection.class.getClassLoader().getResource("page_section2.html").getPath();
    //String url_DE = "file:".concat(path_DE);

    static ChromeDriver driver;

    FormInputs formInputs = new FormInputs("turkey", "1", "0", BLUECARD);

    @BeforeAll
    static void initDriver() {
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
    }

    //@AfterAll
    static void quitDriver() {
        driver.quit();
    }

    @Test
    void ASSERT_THAT_form_successfully_filled_WHEN_run_is_called() throws ElementNotFoundTimeoutException, InteractionFailedException, InterruptedException {
        // GIVEN

        // WHEN
        //Section2ServiceSelection section2ServiceSelection = new Section2ServiceSelection(formInputs, driver);
        //section2ServiceSelection.fillForm();

        Section2ServiceSelectionBAL formFiller = new Section2ServiceSelectionBAL(formInputs, driver);
        //formFiller
        // THEN

    }

    @Test
    void ASSERT_THAT_first_country_is_selected_WHEN_selectCountry_is_called() {
        // GIVEN
        String expectedTime = "09:30";

        // WHEN
        //driver.get(url_DE);
        Section2ServiceSelectionBAL formFiller = new Section2ServiceSelectionBAL(formInputs, driver);

        //underTest.handleSelectingTimeslot();


        // THEN

    }

    /*
    @Test
    void ASSERT_THAT_appointmentSelection_is_not_captured_WHEN_isAppointmentSelectionPageOpened_is_called() {
        // GIVEN
        String path = Section2ServiceSelection.class.getClassLoader().getResource("page_dateSelection2022-12-23_08:11:45.html").getPath();
        String url = "file:".concat(path);

        // WHEN
        webDriver.get(url);
        underTest = new Section2ServiceSelection(formInputs, webDriver);
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
        webDriver.get(url_DE);
        underTest = new Section2ServiceSelection(formInputs, webDriver);
        boolean actualResult;
        try {
            actualResult = underTest.isCalenderOpened();
        } catch (InteractionFailedException e) {
            throw new RuntimeException(e);
        }

        // THEN
        Assertions.assertTrue(actualResult);
    }

     */
}