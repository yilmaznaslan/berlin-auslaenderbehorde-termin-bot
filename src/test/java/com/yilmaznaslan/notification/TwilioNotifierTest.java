package com.yilmaznaslan.notification;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TwilioNotifierTest {

    @Test
    void triggerNotification() {
        // GIVEN
        TwilioNotifier twilioNotifier = new TwilioNotifier();

        // WHEN
        Assertions.assertDoesNotThrow(() -> twilioNotifier.triggerNotification("test"));

        // THEN

    }
}