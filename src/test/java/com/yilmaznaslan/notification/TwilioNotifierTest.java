package com.yilmaznaslan.notification;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TwilioNotifierTest {

    @Test
    void triggerNotification() {
        // GIVEN

        String numberToCallFrom = System.getenv("TWILIO_PHONE_FROM");
        String numberToCall = System.getenv("TWILIO_PHONE_TO");

        TwilioNotifier twilioNotifier = new TwilioNotifier(numberToCall, numberToCallFrom);

        // WHEN
        Assertions.assertDoesNotThrow(() -> twilioNotifier.triggerNotification("test"));

        // THEN

    }
}