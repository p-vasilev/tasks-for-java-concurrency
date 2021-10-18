package ru.nsu.fit.pvasilev.tfjc.task1;

public class Task1 {
    public static void main(String[] args) {
        var childThread = new ChildThread();
        childThread.start();

        for(var i = 1; i < 11; i++) {
            System.out.println("Parent says: " + i);
        }
    }
    private static class ChildThread extends Thread {
        @Override
        public void run() {
            for(var i = 1; i < 11; i++) {
                System.out.println("Child says: " + i);
            }
        }
    }
}
