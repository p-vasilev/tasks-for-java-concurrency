package ru.nsu.fit.pvasilev.tfjc.task10;

public class Task10 {
    public static void main(String[] args) {
        var childLock = new Object();
        var parentLock = new Object();
        var childThread = new Thread(new ChildRunnable(childLock, parentLock));
        childThread.start();

        for(var i = 1; i < 11; i++) {
            synchronized (childLock) {
                childLock.notifyAll();
            }
            synchronized (parentLock) {
                try {
                    parentLock.wait();
                } catch (InterruptedException e) {
                    return;
                }
            }
            System.out.println("Parent says: " + i);
            synchronized (childLock) {
                childLock.notifyAll();
            }
        }
    }
    private static class ChildRunnable implements Runnable {
        private final Object childLock;
        private final Object parentLock;

        private ChildRunnable(Object childLock, Object parentLock) {
            this.childLock = childLock;
            this.parentLock = parentLock;
        }

        public void run() {
            for(var i = 1; i < 11; i++) {
                synchronized (parentLock) {
                    parentLock.notifyAll();
                }
                synchronized(childLock) {
                    try {
                        childLock.wait();
                    } catch (InterruptedException e) {
                        return;
                    }
                }
                System.out.println("Child says: " + i);
                synchronized (parentLock) {
                    parentLock.notifyAll();
                }
            }
        }
    }
}
