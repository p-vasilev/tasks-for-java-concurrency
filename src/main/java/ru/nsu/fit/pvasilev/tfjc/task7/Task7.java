package ru.nsu.fit.pvasilev.tfjc.task7;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class Task7 {
    private static final long ITERATIONS = 1000000000L;
    public static void main(String[] args) {
        var threadCount = Integer.parseInt(args[0]);

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
        for(var t:threadList) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println(resultQueue.stream().reduce(Double::sum).get() * 4);
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
            for(long i = threadNum; i <= ITERATIONS; i+= threadCount) {
                result += (i % 2 == 0 ? 1f : -1f) / (2 * i + 1);
            }
            queue.add(result);
        }
    }
}
