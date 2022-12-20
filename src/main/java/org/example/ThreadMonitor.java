package org.example;


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

        ThreadContext.put("threadCount", String.valueOf(Thread.activeCount()));
        logger.info("Number of threads " + Thread.activeCount());
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
