package ru.nsu.fit.pvasilev.tfjc.task3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Task3 {
    private static final String[] s = {"A", "B", "C", "D"};

    public static void main(String[] args) {
        var stringArray = s;
        startNewThread(1, stringArray);
        stringArray[0] = "hi";
        startNewThread(2, stringArray);
        stringArray[1] = "hello";
        startNewThread(3, stringArray);
        stringArray = new String[]{"1", "2", "3", "4"};
        startNewThread(4, stringArray);
    }

    private static void startNewThread(int id, String[] strings) {
        var stringsClone = strings.clone();
        var thread = new Thread(()->{
           for(var i : stringsClone) {
               System.out.println(id + ": " + i);
           }
        }, String.valueOf(id));
        thread.start();
    }
}
