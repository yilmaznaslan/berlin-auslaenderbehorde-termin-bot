package org.example.notifications;

import com.twilio.rest.api.v2010.account.Call;
import com.twilio.rest.api.v2010.account.Message;
import java.net.URI;

/**
 * Utility class to call a number and to send an SMS
 */
public class TwilioAdapter {

    public static final String ACCOUNT_SID = System.getenv("TWILIO_ACCOUNT_SID");
    public static final String AUTH_TOKEN = System.getenv("TWILIO_AUTH_TOKEN");
    public static final String PHONE_NUMBER_FROM = System.getenv("TWILIO_PHONE_FROM");

    private TwilioAdapter() {
    }

    public static void makeCall(String phoneNumberToBeNotified) {
        com.twilio.Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Call.creator(
                        new com.twilio.type.PhoneNumber(phoneNumberToBeNotified),
                        new com.twilio.type.PhoneNumber(PHONE_NUMBER_FROM),
                        URI.create("http://demo.twilio.com/docs/voice.xml"))
                .create();

    }

    public static void sendSMS(String phoneNumberToBeNotified, String msgBody){
        com.twilio.Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message.creator(
                        new com.twilio.type.PhoneNumber(phoneNumberToBeNotified),
                        new com.twilio.type.PhoneNumber(PHONE_NUMBER_FROM),
                        msgBody)
                .create();

    }

}
