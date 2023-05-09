package org.example.formhandlers;

import org.example.BaseTestSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Section2ServiceSelectionHandlerTest extends BaseTestSetup {

    static String pathToFile_failed = Section2ServiceSelectionHandler.class.getClassLoader().getResource("exception_2023-05-09_06:52:00_exception.html").getPath();

    static String pathToFile_successful = Section2ServiceSelectionHandler.class.getClassLoader().getResource("Section3DateSelectionHandler_2023-03-28_07:18:30_handleTimeSelection.html").getPath();

    static String urlToFile_successful = "file:".concat(pathToFile_successful);

    static String urlToFile_failed = "file:".concat(pathToFile_failed);


    private final Section2ServiceSelectionHandler formFiller = new Section2ServiceSelectionHandler(visaForm_apply_for_bluecard_without_residencePermit, personalInfoFormTO, driver);

    @Test
    void ASSERT_THAT_isCalenderFound_returns_true_WHEN_page_is_opened_and_language_is_english() {
        // GIVEN
        driver.get(urlToFile_successful);

        // WHEN
        boolean actualResult = formFiller.isCalenderFound();

        // THEN
        Assertions.assertTrue(actualResult);
    }

    @Test
    void ASSERT_THAT_sending_form_fails_WHEN_page_is_frozen_and_language_is_english() {
        // GIVEN
        driver.get(urlToFile_failed);

        // WHEN && THEN
        Assertions.assertThrows(Exception.class, () -> formFiller.sendForm() );
    }


}