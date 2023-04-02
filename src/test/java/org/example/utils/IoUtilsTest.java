package org.example.utils;

import org.example.BaseTestSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.remote.RemoteWebDriver;

import static org.example.utils.IoUtils.savePage;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class IoUtilsTest extends BaseTestSetup {

    static RemoteWebDriver remoteWebDriver;


    @BeforeAll
    static void setup() {
        IoUtils.isLocalSaveEnabled = true;
        IoUtils.isS3Enabled = true;
        remoteWebDriver = mock(RemoteWebDriver.class);

    }

    @Test
    void ASSERT_THAT_isCalenderFound_returns_true() {
        // GIVEN
        when(remoteWebDriver.getPageSource()).thenReturn("");

        // WHEN && THEN
        Assertions.assertDoesNotThrow(() -> savePage(driver, "testPage", "demo"));

    }


}