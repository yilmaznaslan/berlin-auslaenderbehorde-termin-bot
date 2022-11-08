package org.example.notifications;

import com.twilio.rest.api.v2010.account.Call;
import com.twilio.rest.api.v2010.account.Message;
import java.net.URI;

public class Twilio {

    public static final String ACCOUNT_SID = System.getenv("TWILIO_ACCOUNT_SID");
    public static final String AUTH_TOKEN = System.getenv("TWILIO_AUTH_TOKEN");
    public static final String PHONE_NUMBER_FROM = System.getenv("TWILIO_PHONE_FROM");

    public static void makeCall(String phoneNumberToBeNotified) {
        com.twilio.Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Call call = Call.creator(
                        new com.twilio.type.PhoneNumber(phoneNumberToBeNotified),
                        new com.twilio.type.PhoneNumber(PHONE_NUMBER_FROM),
                        URI.create("http://demo.twilio.com/docs/voice.xml"))
                .create();

        System.out.println(call.getSid());
    }

    public static void sendSMS(String phoneNumberToBeNotified, String msgBody){
        com.twilio.Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                        new com.twilio.type.PhoneNumber(phoneNumberToBeNotified),
                        new com.twilio.type.PhoneNumber(PHONE_NUMBER_FROM),
                        msgBody)
                .create();

        System.out.println(message.getSid());
    }

}
