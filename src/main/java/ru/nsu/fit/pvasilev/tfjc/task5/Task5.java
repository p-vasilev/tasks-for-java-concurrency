package ru.nsu.fit.pvasilev.tfjc.task5;

public class Task5 {
    public static void main(String[] args) {
        var thread = new Thread(()->{
            while(true) {
                if(Thread.currentThread().isInterrupted()) {
                    System.out.println("Oh no! I was interrupted!");
                    break;
                }
                try {
                    Thread.sleep(300);
                    System.out.println("I'm alive and well! Hooray!");
                } catch (InterruptedException e) {
                    System.out.println("Oh no! I was interrupted!");
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
