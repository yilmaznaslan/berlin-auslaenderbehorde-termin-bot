package com.yilmaznaslan.notification;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;

public class TwilioNotifier implements NotificationAdapter {

    private static final Logger logger = LoggerFactory.getLogger(TwilioNotifier.class);

    private final String numberToCall;
    private final String numberToCallFrom;
    private static final String ACCOUNT_SID = System.getenv("TWILIO_ACCOUNT_SID");
    private static final String AUTH_TOKEN = System.getenv("TWILIO_AUTH_TOKEN");

    public TwilioNotifier(String numberToCall, String numberToCallFrom) {
        this.numberToCall = numberToCall;
        this.numberToCallFrom = numberToCallFrom;
    }

    @Override
    public void triggerNotification(String message) {
        try {
            makeCall();
        } catch (Exception e) {
            logger.info("Error while making call: {}", e.getMessage());

        }

    }

    protected void makeCall() throws URISyntaxException {

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Call call = Call
                .creator(
                        new PhoneNumber(numberToCall),
                        new PhoneNumber(numberToCallFrom),
                        new URI("http://demo.twilio.com/docs/voice.xml")
                )
                .create();

        logger.info("Call status :{}", call.getStatus());
    }

}
