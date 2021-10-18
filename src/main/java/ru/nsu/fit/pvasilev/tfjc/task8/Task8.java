package ru.nsu.fit.pvasilev.tfjc.task8;

import sun.misc.Signal;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class Task8 {
    static int threadCount;
    static Thread mainThread;
    private static final Object maxIterationsLock = new Object();
    private static volatile long maxIterations = 0L;
    public static void main(String[] args) {
        mainThread = Thread.currentThread();

        threadCount = Integer.parseInt(args[0]);

        if (threadCount <= 0) {
            System.out.println("???");
            return;
        }

        var resultQueue = new ArrayBlockingQueue<Double>(threadCount);
        var threadList = new ArrayList<Thread>();

        for(int i = 0; i < threadCount; i++) {
            threadList.add(new Thread(new LeibnizRunnable(i, threadCount, resultQueue)));
        }
        for(var t:threadList) {
            t.start();
        }


        Signal.handle(new Signal("INT"), (Signal sig) ->{
           System.out.println("Honey, the " + sig.getName() + " signal has arrived!");
           System.out.println("Time for your thread interruption!");
           mainThread.interrupt();
        });

        var punkThread = new Thread(()->{
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                Signal.raise(new Signal("INT"));
            }
            Signal.raise(new Signal("INT"));
        });
        punkThread.start();

        try {
            var o = new Object();
            synchronized (o){
                o.wait();
            }
        } catch (InterruptedException e) {
            for(var t:threadList) {
                t.interrupt();
            }
            for(var t:threadList) {
                try {
                    t.join();
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println(resultQueue.stream().reduce(Double::sum).get() * 4);
            System.out.println(maxIterations);
        }

    }

    private static class LeibnizRunnable implements Runnable {
        private final int threadNum;
        private final int threadCount;
        private final Queue<Double> queue;

        public LeibnizRunnable(int threadNum, int threadCount, Queue<Double> queue) {
            this.threadNum = threadNum;
            this.threadCount = threadCount;
            this.queue = queue;
        }
        public void run() {
            double result = 0;
            long iterationCount = threadNum;
            while(!Thread.currentThread().isInterrupted() || iterationCount < maxIterations) {
                result += (iterationCount % 2 == 0 ? 1f : -1f) / (2 * iterationCount + 1);
                iterationCount += threadCount;
                //BAD!
                synchronized (maxIterationsLock) {
                    if (iterationCount > maxIterations) {
                        maxIterations = iterationCount;
                    }
                }
            }
            while(iterationCount < maxIterations) {
                result += (iterationCount % 2 == 0 ? 1f : -1f) / (2 * iterationCount + 1);
                iterationCount += threadCount;
            }
            queue.add(result);
        }
    }
}
