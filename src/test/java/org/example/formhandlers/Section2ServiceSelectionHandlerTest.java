package org.example.formhandlers;

import org.example.BaseTestSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Section2ServiceSelectionHandlerTest extends BaseTestSetup {

    static String pathToFile_en_successful = Section2ServiceSelectionHandler.class.getClassLoader().getResource("Section3DateSelectionHandler_2023-03-28_07:18:30_handleTimeSelection.html").getPath();

    static String urlToFile_en = "file:".concat(pathToFile_en_successful);

    private final Section2ServiceSelectionHandler formFiller = new Section2ServiceSelectionHandler(visaForm_apply_for_bluecard_without_residencePermit, personalInfoFormTO, driver);

    @Test
    void ASSERT_THAT_isCalenderFound_returns_true_WHEN_page_is_opened_and_language_is_english() {
        // GIVEN
        driver.get(urlToFile_en);

        // WHEN
        boolean actualResult = formFiller.isCalenderFound();

        // THEN
        Assertions.assertTrue(actualResult);
    }


}