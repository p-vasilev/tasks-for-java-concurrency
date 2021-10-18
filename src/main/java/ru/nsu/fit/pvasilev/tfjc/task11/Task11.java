package ru.nsu.fit.pvasilev.tfjc.task11;

import java.util.concurrent.Semaphore;

public class Task11 {
    public static void main(String[] args) {
        var sem1 = new Semaphore(0);
        var sem2 = new Semaphore(0);
        var childThread = new Thread(new ChildRunnable(sem1, sem2));

        sem1.release();

        childThread.start();

        for(var i = 1; i < 11; i++) {
            try {
                sem1.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Parent says: " + i);
            sem2.release();
        }
    }
    private static class ChildRunnable implements Runnable {
        private final Semaphore sem1;
        private final Semaphore sem2;

        private ChildRunnable(Semaphore childLock, Semaphore parentLock) {
            this.sem1 = childLock;
            this.sem2 = parentLock;
        }

        public void run() {
            for(var i = 1; i < 11; i++) {
                try {
                    sem2.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Child says: " + i);
                sem1.release();
            }
        }
    }
}
