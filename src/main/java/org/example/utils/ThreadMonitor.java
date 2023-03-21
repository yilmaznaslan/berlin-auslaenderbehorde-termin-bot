package org.example.utils;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import java.util.Timer;
import java.util.TimerTask;

public class ThreadMonitor extends TimerTask {
    private final Logger logger = LogManager.getLogger(ThreadMonitor.class);
    private final Timer timer = new Timer(true);

    public void startMonitoring() {
        timer.scheduleAtFixedRate(this, 0,30000);
    }
    @Override
    public void run() {
        int threadCount = Thread.activeCount();
        ThreadContext.put("threadCount", String.valueOf(threadCount));
        logger.info(String.format("Number of threads: %s", threadCount));
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            logger.error(String.format("Some error occurred during monitoring the threads. Reason: ", e));
        }
    }

}
