package ru.nsu.fit.pvasilev.tfjc.task4;

public class Task4 {
    public static void main(String[] args) {
        var thread = new Thread(()->{
            while(true) {
                if(Thread.currentThread().isInterrupted()) {
                    break;
                }
                try {
                    Thread.sleep(300);
                    System.out.println("I'm alive and well! Hooray!");
                } catch (InterruptedException e) {
                    break;
                }
            }
        }, "Child");

        thread.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.interrupt();
    }
}
