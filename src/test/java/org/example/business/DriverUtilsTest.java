package org.example.business;

import org.junit.jupiter.api.Test;

import static org.example.utils.DriverUtils.saveSourceCodeToFile;

class DriverUtilsTest {

    @Test
    void saveSourceCodeToFileTest() {
        saveSourceCodeToFile("asdasd", DriverUtilsTest.class.getSimpleName(), "after");
    }


}