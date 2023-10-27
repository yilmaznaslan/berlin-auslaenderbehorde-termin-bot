package com.yilmaznaslan.notification;

public class AllAdapters implements NotificationAdapter {

    private final TwilioNotifier twilioNotifier;
    private final SlackNotifier slackNotifier;
    private final SoundNotifier soundNotifier;

    public AllAdapters(SoundNotifier soundNotifier, TwilioNotifier twilioNotifier, SlackNotifier slackNotifier) {
        this.soundNotifier = soundNotifier;
        this.twilioNotifier = twilioNotifier;
        this.slackNotifier = slackNotifier;
    }

    @Override
    public void triggerNotification(String message) {
        soundNotifier.triggerNotification(message);
        twilioNotifier.triggerNotification(message);
        slackNotifier.triggerNotification(message);
    }
}
