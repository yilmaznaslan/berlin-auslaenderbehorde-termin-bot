package com.yilmaznaslan.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class SlackNotifier implements NotificationAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SlackNotifier.class);
    private static final String SLACK_WEBHOOK_URL = System.getenv().get("SLACK_WEBHOOK_URL");
    private static final int MAX_RETRY = 10;
    private int tryCount = 0;


    @Override
    public void triggerNotification(String message) {
        try {
            tryCount++;

            URL url = new URL(SLACK_WEBHOOK_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");

            String payload = "{\"text\":\"" + message + "\"}";

            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(payload.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = connection.getResponseCode();
            LOGGER.info("Response code: {}", responseCode);

            if (tryCount < MAX_RETRY && responseCode != 200) {
                Thread.sleep(1000);
                triggerNotification(message);
            }

        } catch (Exception e) {
            LOGGER.error("Error while sending slack notification: {}", e.getMessage());
        }
    }
}
