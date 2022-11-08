package org.example;

public class ThreadMonitor implements Runnable{
    @Override
    public void run() {
        while(true){
            System.out.println("Number of threads " + Thread.activeCount());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
