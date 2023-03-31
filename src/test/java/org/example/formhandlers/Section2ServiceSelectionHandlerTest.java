package org.example.formhandlers;

import org.example.BaseTestSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Section2ServiceSelectionHandlerTest extends BaseTestSetup {

    static String pathToFile_en_successful = Section2ServiceSelectionHandler.class.getClassLoader().getResource("book appointment - Service selection.html").getPath();

    static String urlToFile_en = "file:".concat(pathToFile_en_successful);

    private final Section2ServiceSelectionHandler formFiller = new Section2ServiceSelectionHandler(visaForm_apply_for_bluecard_without_residencePermit, personalInfoFormTO, driver);

    @Test
    void ASSERT_THAT_service_type_is_selected() {
        // GIVEN
        driver.get(urlToFile_en);

        // WHEN & THEN
        Assertions.assertDoesNotThrow(formFiller::clickServiceType);
    }

}