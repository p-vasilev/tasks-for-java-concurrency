package ru.nsu.fit.pvasilev.tfjc.task2;

public class Task2 {
    public static void main(String[] args) {
        var childThread = new ChildThread();
        childThread.start();

        try {
            childThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        for(var i = 0; i < 10; i++) {
            System.out.println("Parent says: " + i);
        }
    }
    private static class ChildThread extends Thread {
        @Override
        public void run() {
            for(var i = 0; i < 10; i++) {
                System.out.println("Child says: " + i);
            }
        }
    }
}
