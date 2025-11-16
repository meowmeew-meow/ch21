package j;

import java.util.concurrent.TimeUnit;

//962 22
public class NotActiveWaiting {
    static boolean b;
    public synchronized Thread task1(Thread t) {
        return new Thread() {
            public synchronized void run() {
                try {
                    System.out.println("Task1");
                    TimeUnit.SECONDS.sleep(10);
                    synchronized(t) {
                        b = true;
                        System.out.println("Task1 complete "+b);
                        t.notifyAll();
                    }
                } catch (InterruptedException e) {
                    System.out.println("Interrupted");
                }
            }
        };
    }

    public synchronized Thread task2() {
        return new Thread() {
            public synchronized void run() {
                try {
                    System.out.println("Task2");
                    while (b == false) {
                        wait();
                    }
                    b = false;
                    System.out.println("the flag has been changed "+ b);
                } catch (InterruptedException e) {
                    System.out.println("Interrupted");
                }
            }
        };
    }

    public static void main(String[] args) throws InterruptedException {
        NotActiveWaiting aw = new NotActiveWaiting();
        Thread t2 = aw.task2();
        Thread t1 = aw.task1(t2);
        t2.start();
        t1.start();
        TimeUnit.SECONDS.sleep(20);
        t1.interrupt();
        t2.interrupt();
    }
}
