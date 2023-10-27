package com.yilmaznaslan.notification;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SlackNotifierTest {


    @Test
    void triggerNotification() {
        SlackNotifier slackNotifier = new SlackNotifier();
        slackNotifier.triggerNotification("https://otv.verwalt-berlin.de/ams/TerminBuchen/wizardng/84836c62-6d29-4534-a683-1ac923e5b45b?dswid=8146&dsrid=38");
    }
}