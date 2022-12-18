package org.example;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ThreadMonitor implements Runnable{
    private final Logger logger = LogManager.getLogger(ThreadMonitor.class);

    @Override
    public void run() {
        while(true){
            logger.info("Number of threads " + Thread.activeCount());
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
