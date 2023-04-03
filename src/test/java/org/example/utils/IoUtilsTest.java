package org.example.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.RemoteWebDriver;

import static org.example.utils.IoUtils.savePage;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class IoUtilsTest {

    private final RemoteWebDriver remoteWebDriver = mock(RemoteWebDriver.class);


    @BeforeAll
    static void setup() {
        IoUtils.isLocalSaveEnabled = true;
        IoUtils.isS3Enabled = true;

    }

    @Test
    void ASSERT_THAT_save_is_completed_without_error_GIVEN_driveR_throws_exception() {
        // GIVEN
        when(remoteWebDriver.getPageSource()).thenThrow(new WebDriverException());

        // WHEN && THEN
        Assertions.assertDoesNotThrow(() -> savePage(remoteWebDriver, "testPage", "demo"));

    }


}