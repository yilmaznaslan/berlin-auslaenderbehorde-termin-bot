package org.example.formhandlers;

import org.example.BaseTestSetup;
import org.example.exceptions.ElementNotFoundTimeoutException;
import org.example.utils.DriverUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import static org.mockito.Mockito.verify;

class Section3DateSelectionHandlerTest extends BaseTestSetup {

    static String pathToFile_de = Section3DateSelectionHandler.class.getClassLoader().getResource("dateSelection_DE.html").getPath();
    static String pathToFile_en = Section3DateSelectionHandler.class.getClassLoader().getResource("Section3DateSelectionHandler_2023-03-27_11:09:38_handleTimeSelection.html").getPath();

    static String urlToFile_de = "file:".concat(pathToFile_de);
    static String urlToFile_en = "file:".concat(pathToFile_en);

    private final Section3DateSelectionHandler formFiller = new Section3DateSelectionHandler(driver);

    @Test
    void ASSERT_THAT_isCalenderFound_returns_true_WHEN_page_is_opened_and_language_is_deutsch() throws InterruptedException {
        // GIVEN
        driver.get(urlToFile_de);

        // WHEN
        boolean actualResult = formFiller.isCalenderFound();

        // THEN
        Assertions.assertTrue(actualResult);
    }

    @Test
    void ASSERT_THAT_isCalenderFound_returns_true_WHEN_page_is_opened_and_language_is_english() throws InterruptedException {
        // GIVEN
        driver.get(urlToFile_en);

        // WHEN
        boolean actualResult = formFiller.isCalenderFound();

        // THEN
        Assertions.assertTrue(actualResult);
    }

    @Test
    void ASSERT_THAT_isDateVerified_returns_true() throws ElementNotFoundTimeoutException, InterruptedException {
        // GIVEN
        driver.get(urlToFile_de);
        String cssSelector = "[data-handler=selectDay]";
        WebElement element = DriverUtils.getElementByCssSelector(cssSelector, "elementDescription", driver);

        // WHEN
        boolean actualResult = formFiller.isDateVerified(element);

        // THEN
        Assertions.assertTrue(actualResult);
    }

    @Test
    void ASSERT_THAT_isDateVerified_returns_true_WHEN_page_language_is_english() throws ElementNotFoundTimeoutException, InterruptedException {
        // GIVEN
        driver.get(urlToFile_en);
        String cssSelector = "[data-handler=selectDay]";
        WebElement element = DriverUtils.getElementByCssSelector(cssSelector, "elementDescription", driver);

        // WHEN
        boolean actualResult = formFiller.isDateVerified(element);

        // THEN
        Assertions.assertTrue(actualResult);
    }

    @Test
    void ASSERT_THAT_date_is_clicked_WHEN_date_is_verified_AND_page_language_is_german() {
        // GIVEN
        driver.get(urlToFile_de);

        // WHEN & THEN
        Assertions.assertDoesNotThrow(formFiller::handleAppointmentSelection);
    }

    @Test
    void ASSERT_THAT_date_is_clicked_WHEN_date_is_verified_AND_page_language_is_english() {
        // GIVEN
        driver.get(urlToFile_en);

        // WHEN & THEN
        Assertions.assertDoesNotThrow(formFiller::handleAppointmentSelection);
    }

    @Test
    void ASSERT_THAT_time_is_not_verified_WHEN_date_is_verified_AND_page_language_is_english() {
        // GIVEN
        driver.get(urlToFile_en);
        formFiller.handleAppointmentSelection();
        Select select = formFiller.getAvailableTimeslotOptions();

        // WHEN
        boolean isTimeslotVerified = formFiller.isTimeslotOptionVerified(select);

        // THEN
        Assertions.assertFalse(isTimeslotVerified);
    }

    @Test
    void ASSERT_THAT_time_is_not_enabled_WHEN_date_is_verified_AND_page_language_is_english() {
        // GIVEN
        driver.get(urlToFile_en);
        formFiller.handleAppointmentSelection();
        Select select = formFiller.getAvailableTimeslotOptions();

        // WHEN && THEN
        Assertions.assertThrows(Exception.class, () -> formFiller.getAvailableTimeslotOptions());
    }

    @Test
    void ASSERT_THAT_time_is_verified_WHEN_date_is_verified_AND_page_language_is_german() {
        // GIVEN
        driver.get(urlToFile_de);
        formFiller.handleAppointmentSelection();
        Select select = formFiller.getAvailableTimeslotOptions();

        // WHEN
        boolean isTimeslotVerified = formFiller.isTimeslotOptionVerified(select);

        // THEN
        Assertions.assertTrue(isTimeslotVerified);
    }

    @Test
    void ASSERT_THAT_time_is_selected_WHEN_date_is_verified_AND_page_language_is_german() {
        // GIVEN
        driver.get(urlToFile_de);
        formFiller.handleAppointmentSelection();
        Select select = formFiller.getAvailableTimeslotOptions();
        String expectedSelectedTime = "09:30";

        // WHEN & THEN
        Assertions.assertDoesNotThrow(() -> formFiller.selectTimeslot(select));
        String actualSelectedTime = select.getFirstSelectedOption().getText();

        // THEN
        Assertions.assertEquals(expectedSelectedTime, actualSelectedTime);
    }

    @Test
    void ASSERT_THAT_time_is_selected_WHEN_date_is_verified_AND_page_language_is_english() {
        // GIVEN
        driver.get(urlToFile_en);
        formFiller.handleAppointmentSelection();
        Select select = formFiller.getAvailableTimeslotOptions();
        String expectedSelectedTime = "10:00";

        // WHEN & THEN
        Assertions.assertDoesNotThrow(() -> formFiller.selectTimeslot(select));
        String actualSelectedTime = select.getFirstSelectedOption().getText();

        // THEN
        Assertions.assertEquals(expectedSelectedTime, actualSelectedTime);
    }

    @Test
    void ASSERT_THAT_form_is_send_WHEN_date_and_time_is_selected_AND_page_language_is_german() throws Exception {
        // GIVEN
        driver.get(urlToFile_de);

        // WHEN & THEN
        Section3DateSelectionHandler spiedFormFiller = Mockito.spy(new Section3DateSelectionHandler(driver));
        spiedFormFiller.fillAndSendForm();

        // THEN
        verify(spiedFormFiller).handleAppointmentSelection();
        verify(spiedFormFiller).handleTimeSelection();
        verify(spiedFormFiller).sendForm();

    }
}