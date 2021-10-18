package ru.nsu.fit.pvasilev.tfjc.task13;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Task13 {
    private static final int PHILOSOPHER_COUNT = 6;
    static List<Philosopher> philosopherList = new ArrayList<>();
    static List<ReentrantLock> forkList = new ArrayList<>();
    static List<Object> philosopherLocks = new ArrayList<>();
    static final Object forksLock = new Object();
    public static void main(String[] args) {
        for(var i = 0; i < PHILOSOPHER_COUNT; i++) {
            forkList.add(new ReentrantLock());
        }
        for(var i = 0; i < PHILOSOPHER_COUNT; i++) {
            philosopherList.add(new Philosopher(forkList.get(i), forkList.get((i + 1) % PHILOSOPHER_COUNT)));
        }
        List<Thread> threadList = new ArrayList<>();
        for(var i : philosopherList){
            threadList.add(new Thread(i));
        }
        for(var t: threadList) {
            t.start();
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(var t: threadList) {
            t.interrupt();
        }
    }

    private static class Philosopher implements Runnable{
        private static final long SLEEP_TIME = 200;
        private static final long SLEEP_TIME_RANDOM_RANGE = 150;
        private final Random random = new Random();
        private final Lock leftFork;
        private final Lock rightFork;

        Philosopher(Lock lock1, Lock lock2) {
            leftFork = lock1;
            rightFork = lock2;
        }

        @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()) {
                sleep();
                say("I've been thinking enough, it's lunchtime!");
                boolean hasBothForks = false;
                while(!hasBothForks) {
                    synchronized (forksLock) {
                        if (leftFork.tryLock()) {
                            say("I took the fork on the left");
                            if (rightFork.tryLock()) {
                                say("I took the fork on the right, now i feast");
                                forksLock.notifyAll();
                                hasBothForks = true;
                            } else {
                                try {
                                    say("Cant get right fork, now i return the left one and wait");
                                    leftFork.unlock();
                                    forksLock.notifyAll();
                                    forksLock.wait();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                    return;
                                }
                            }
                        } else {
                            try {
                                say("Cant get left fork, waiting for it");
                                forksLock.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                return;
                            }
                        }
                    }
                }
                sleep();
                leftFork.unlock();
                rightFork.unlock();
                say("Lunch's over! Time to think");
            }
        }

        private void say(String s) {
            System.out.println(Thread.currentThread().getName() + ": " + s);
        }

        private void sleep() {
            try {
                Thread.sleep(SLEEP_TIME + random.nextLong() % SLEEP_TIME_RANDOM_RANGE);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
