package org.example.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Timer;
import java.util.TimerTask;

public class ThreadMonitor extends TimerTask {
    private final Logger logger = LoggerFactory.getLogger(ThreadMonitor.class);
    private final Timer timer = new Timer(true);

    public void startMonitoring() {
        timer.scheduleAtFixedRate(this, 0,30000);
    }
    @Override
    public void run() {
        int threadCount = Thread.activeCount();
        MDC.put("threadCount", String.valueOf(threadCount));
        logger.info(String.format("Number of threads: %s", threadCount));
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            logger.error(String.format("Some error occurred during monitoring the threads. Reason: ", e));
        }
    }

}
