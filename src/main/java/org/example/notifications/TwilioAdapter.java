package org.example.notifications;

import com.twilio.rest.api.v2010.account.Call;
import com.twilio.rest.api.v2010.account.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;

/**
 * Utility class to call a number and to send an SMS
 */
public class TwilioAdapter {

    public static final String ACCOUNT_SID = System.getenv("TWILIO_ACCOUNT_SID");
    public static final String AUTH_TOKEN = System.getenv("TWILIO_AUTH_TOKEN");
    public static final String PHONE_NUMBER_FROM = System.getenv("TWILIO_PHONE_FROM");
    private static final Logger logger = LogManager.getLogger(TwilioAdapter.class);

    private TwilioAdapter() {
    }

    public static void makeCall(String phoneNumberToBeNotified) {
        com.twilio.Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        try{
            Call.creator(
                            new com.twilio.type.PhoneNumber(phoneNumberToBeNotified),
                            new com.twilio.type.PhoneNumber(PHONE_NUMBER_FROM),
                            URI.create("http://demo.twilio.com/docs/voice.xml"))
                    .create();
        } catch (Exception e){
            logger.warn("Failed to make Call", e);
        }

    }

    public static void sendSMS(String phoneNumberToBeNotified, String msgBody){
        com.twilio.Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        try{
            Message.creator(
                            new com.twilio.type.PhoneNumber(phoneNumberToBeNotified),
                            new com.twilio.type.PhoneNumber(PHONE_NUMBER_FROM),
                            msgBody)
                    .create();
        }catch (Exception e){
        logger.warn("Failed to send SMS", e);
        }

    }

}
