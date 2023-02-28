package org.example.auslanderbehorde.formfiller.business;

import org.junit.jupiter.api.Test;

import static com.amazonaws.SDKGlobalConfiguration.ACCESS_KEY_ENV_VAR;
import static org.example.auslanderbehorde.formfiller.business.FormFillerUtils.saveScreenshot;
import static org.example.auslanderbehorde.formfiller.business.FormFillerUtils.saveSourceCodeToFile;
import static org.junit.jupiter.api.Assertions.*;

class FormFillerUtilsTest {

    @Test
    void saveSourceCodeToFileTest() {
        saveSourceCodeToFile("asdasd", FormFillerUtilsTest.class.getSimpleName(), "after");
    }


}