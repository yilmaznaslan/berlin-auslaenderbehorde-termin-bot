package org.example.business.formhandlers;

import org.example.BaseTestSetup;
import org.example.exceptions.ElementNotFoundTimeoutException;
import org.example.utils.DriverUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

class Section3DateSelectionHandlerTest extends BaseTestSetup {

    static String pathToFile_de = Section4VisaFormHandler.class.getClassLoader().getResource("page_section3_dateSelection_2023-02-03_10:23:27.html").getPath();
    static String pathToFile_en = Section4VisaFormHandler.class.getClassLoader().getResource("Section3DateSelectionHandler_2023-03-23_00_35_11_handling_date.html").getPath();

    static String urlToFile_de = "file:".concat(pathToFile_de);
    static String urlToFile_en = "file:".concat(pathToFile_en);

    Section3DateSelectionHandler formFiller = new Section3DateSelectionHandler(driver);

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
        Assertions.assertDoesNotThrow(() -> formFiller.handleFindingDate());
    }

    @Test
    void ASSERT_THAT_date_is_clicked_WHEN_date_is_verified_AND_page_language_is_english() {
        // GIVEN
        driver.get(urlToFile_en);

        // WHEN & THEN
        Assertions.assertDoesNotThrow(() -> formFiller.handleFindingDate());
    }
}