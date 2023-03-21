package org.example.business;

import org.junit.jupiter.api.Test;

import static org.example.utils.FormFillerUtils.saveSourceCodeToFile;

class FormFillerUtilsTest {

    @Test
    void saveSourceCodeToFileTest() {
        saveSourceCodeToFile("asdasd", FormFillerUtilsTest.class.getSimpleName(), "after");
    }


}