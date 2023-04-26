package org.example.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.RemoteWebDriver;

import static org.example.utils.IoUtils.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class IoUtilsTest {

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
    void asd() throws InterruptedException {

        int sent_metric_count = 20;
        while (sent_metric_count != 0) {
            increaseReservationDoneMetric();
            Thread.sleep(5000);
            sent_metric_count = sent_metric_count - 1;
        }
    }

}