package j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
//908 9

class PersonalThreadFactory implements ThreadFactory{
    int priority=0;
    public PersonalThreadFactory(int priority){
        this.priority=priority;
    }
    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r);
        t.setPriority(priority);
        return t;
    }
}

public class SimplePriorities implements Runnable {
    private int countDown = 5;
    private volatile double d; // Без оптимизации

    public String toString() {
        return Thread.currentThread() + ": " + countDown;
    }

    public void run() {
        while (true) {
            for (int i = 1; i < 100000; i++) {
                d += (Math.PI + Math.E) / (double) i;
                if (i % 1000 == 0)
                    Thread.yield();
            }
            System.out.println(this);
            if (--countDown == 0) return;
        }
    }

    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        PersonalThreadFactory threadFactory = new PersonalThreadFactory(Thread.MIN_PRIORITY);
        PersonalThreadFactory threadFactory1=new PersonalThreadFactory(Thread.MAX_PRIORITY);
        for (int i = 0; i < 5; i++) {
            exec.execute(threadFactory.newThread(new SimplePriorities()));
        }
        exec.execute(threadFactory1.newThread(new SimplePriorities()));
        exec.shutdown();
    }
}