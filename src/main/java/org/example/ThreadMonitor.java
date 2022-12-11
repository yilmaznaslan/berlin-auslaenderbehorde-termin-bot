package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadMonitor implements Runnable{
    private final Logger logger = LoggerFactory.getLogger(ThreadMonitor.class);

    @Override
    public void run() {
        while(true){
            logger.debug("Number of threads " + Thread.activeCount());
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
