package com.yilmaznaslan.notification;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SoundNotifier implements NotificationAdapter {
    private static final Logger logger = LoggerFactory.getLogger(SoundNotifier.class);

    @Override
    public void triggerNotification(String message) {
        String os = System.getProperty("os.name").toLowerCase();
        try {
            if (os.contains("win")) {
                String[] cmd = {
                        "powershell",
                        "Add-Type -AssemblyName System.Speech; $speak = New-Object System.Speech.Synthesis.SpeechSynthesizer; $speak.Speak('Alert! Attention required!');"
                };
                Runtime.getRuntime().exec(cmd);
            } else if (os.contains("mac")) {
                Runtime.getRuntime().exec(new String[]{"say", "Alert! Attention required!"});
            } else {
                // Handle other operating systems
            }
        } catch (IOException e) {
            logger.error("Failed to notify. Reason: ", e);
        }
    }
}
