package ru.nsu.fit.pvasilev.tfjc.task12;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class Task12 {
    private static final LinkedList<String> stringList = new LinkedList<>();

    public static void main(String[] args) {
        var childThread = new Thread(new ChildRunnable());
        childThread.start();
        var reader = new BufferedReader(new InputStreamReader(System.in));
        String str;
        while(!Thread.currentThread().isInterrupted()) {
            try {
                str = reader.readLine();
                synchronized (stringList) {
                    if (str.isEmpty()) {
                        for (var s : stringList) {
                            System.out.println(s);
                        }
                    } else {

                        stringList.addFirst(str);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        childThread.interrupt();
    }

    private static class ChildRunnable implements Runnable{
        public void run(){
            while(!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (stringList){
                    stringList.sort(String::compareTo);
                }
            }
        }
    }
}
