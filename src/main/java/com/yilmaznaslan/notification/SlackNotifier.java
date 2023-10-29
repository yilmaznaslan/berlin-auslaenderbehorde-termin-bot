package com.yilmaznaslan.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SlackNotifier implements NotificationAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SlackNotifier.class);
    private static final String SLACK_WEBHOOK_URL = System.getenv().get("SLACK_WEBHOOK_URL");
    private static final int MAX_RETRY = 50;
    private int tryCount = 0;


    @Override
    public void triggerNotification(String message) {
        try {
            tryCount++;
            LOGGER.info("Sending slack notification: {}", message);

            int responseCode = sendMessage(message);
            if (tryCount < MAX_RETRY && responseCode != 200) {
                Thread.sleep(1000);
                triggerNotification(message);
            }

        } catch (Exception e) {
            LOGGER.error("Error while sending slack notification: {}", e.getMessage());
        }
    }

    private int sendMessage(String message) {
        OkHttpClient client = new OkHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();
        String json;
        try {
            json = objectMapper.writeValueAsString(new SlackPostTO(message));
            LOGGER.info("Sending slack notification: {}", json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        RequestBody body = RequestBody.create(json, okhttp3.MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(SLACK_WEBHOOK_URL)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            int responseCode = response.code();
            LOGGER.info("ResponseCode: {}, ResponseBody: {}", responseCode, response.body());
            return responseCode;
        } catch (IOException e) {
            LOGGER.error("Error while sending slack notification: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
