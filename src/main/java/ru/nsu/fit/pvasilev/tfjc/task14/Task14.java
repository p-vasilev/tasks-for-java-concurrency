package ru.nsu.fit.pvasilev.tfjc.task14;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Semaphore;

public class Task14 {
    static int widgetCount = 0;
    static Semaphore partASem = new Semaphore(0);
    static Semaphore partBSem = new Semaphore(0);
    static Semaphore partCSem = new Semaphore(0);
    public static void main(String[] args) {
        var partAProducer = new Thread(new PartAProducerRunnable());
        var partBProducer = new Thread(new PartBProducerRunnable());
        var partCProducer = new Thread(new PartCProducerRunnable());
        var notifierThread = new Thread(new NotifierRunnable());
        partAProducer.start();
        partBProducer.start();
        partCProducer.start();
        notifierThread.start();
        while(!Thread.currentThread().isInterrupted()) {
            try {
                partASem.acquire();
                partBSem.acquire();
                partCSem.acquire();
                widgetCount++;
            } catch (InterruptedException e) {
                break;
            }
        }
        partAProducer.interrupt();
        partBProducer.interrupt();
        partCProducer.interrupt();
        notifierThread.interrupt();
    }
    private static class PartAProducerRunnable implements Runnable{
        public void run() {
            while(!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    return;
                }
                partASem.release();
            }
        }
    }
    private static class PartBProducerRunnable implements Runnable{
        public void run() {
            while(!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    return;
                }
                partBSem.release();
            }
        }
    }
    private static class PartCProducerRunnable implements Runnable{
        public void run() {
            while(!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    return;
                }
                partCSem.release();
            }
        }
    }
    private static class NotifierRunnable implements Runnable{
        public void run() {
            var reader = new BufferedReader(new InputStreamReader(System.in));
            while(!Thread.currentThread().isInterrupted()) {
                try {
                    reader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("There are currently " +
                        widgetCount + " widgets, " +
                        partASem.availablePermits() + " parts A, " +
                        partBSem.availablePermits() + " parts B, and " +
                        partCSem.availablePermits() + " parts C");
            }
        }
    }
}
