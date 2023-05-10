package org.example.utils;

import org.example.BaseTestSetup;
import org.example.formhandlers.Section2ServiceSelectionHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.RemoteWebDriver;

import static org.example.utils.IoUtils.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class IoUtilsTest extends BaseTestSetup {

    static String pathToFile_en_successful = Section2ServiceSelectionHandler.class.getClassLoader().getResource("Section3DateSelectionHandler_2023-03-28_07:18:30_handleTimeSelection.html").getPath();
    static String urlToFile_en = "file:".concat(pathToFile_en_successful);
    private final RemoteWebDriver remoteWebDriver = mock(RemoteWebDriver.class);

    @BeforeAll
    static void setup() {
        IoUtils.isLocalSaveEnabled = true;
        IoUtils.isS3Enabled = true;
        setAWSCredentials();
    }

    @Test
    void ASSERT_THAT_save_is_completed_without_error_GIVEN_driveR_throws_exception() {
        // GIVEN
        when(remoteWebDriver.getPageSource()).thenThrow(new WebDriverException());

        // WHEN && THEN
        Assertions.assertDoesNotThrow(() -> savePage(remoteWebDriver, "testPage", "demo"));

    }

    @Test
    void ASSERT_THAT_save_is_completed_without_error_GIVEN_driver_does_not_throws_exception() {
        // GIVEN

        // WHEN &&
        driver.get(urlToFile_en);

        // THEN
        Assertions.assertDoesNotThrow(() -> savePage(driver, "testPage", "demo"));

    }


    @Test
    void asd() throws InterruptedException {
        sendEventToAWS(new EventDetail("reservation", "completed"));
    }


}