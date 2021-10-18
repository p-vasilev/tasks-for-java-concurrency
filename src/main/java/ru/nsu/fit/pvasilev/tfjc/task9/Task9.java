package ru.nsu.fit.pvasilev.tfjc.task9;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Task9 {
    private static final int PHILOSOPHER_COUNT = 6;
    public static void main(String[] args) {
        List<Philosopher> philosopherList = new ArrayList<>();
        List<Object> forkList = new ArrayList<>();
        for(var i = 0; i < PHILOSOPHER_COUNT; i++) {
            forkList.add(new Object());
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
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(var t: threadList) {
            t.interrupt();
        }
    }

    private static class Philosopher implements Runnable{
        private static final long SLEEP_TIME = 1000;
        private static final long SLEEP_TIME_RANDOM_RANGE = 300;
        private final Random random = new Random();
        private final Object leftFork;
        private final Object rightFork;

        Philosopher(Object lock1, Object lock2) {
            leftFork = lock1;
            rightFork = lock2;
        }

        @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()) {
                sleep();
                say("I've been thinking enough, it's lunchtime!");
                synchronized (leftFork) {
                    say("I took the fork on the left");
                    synchronized (rightFork) {
                        say("I took the fork on the right");
                        sleep();
                    }
                }
            }
            say("Lunch's over! Time to think");
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
