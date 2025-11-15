package j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

//961 21
class Task1 implements Runnable{
    Task2 t;
    Task1(Task2 t){
        this.t=t;
    }
    @Override
    public synchronized void run() {
        try {
            System.out.println("Task1");
            synchronized (t) {
                TimeUnit.SECONDS.sleep(5);
                t.cancel();
                t.notifyAll();
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
    }
}
class Task2 implements Runnable{
    public boolean b = false;
    public synchronized void cancel(){
        b=true;
    }
    @Override
    public synchronized void run() {
        try {
            System.out.println("Task2");
            while(b==false) {
                wait();
            }
            System.out.println("Task complete");
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
    }
}
public class Waiting {
    public static void main(String[] args) throws InterruptedException{
        ExecutorService exec = Executors.newCachedThreadPool();
        Task2 task2 = new Task2();
        exec.execute(task2);
        TimeUnit.SECONDS.sleep(10);
        exec.execute(new Task1(task2));
        TimeUnit.SECONDS.sleep(20);
        exec.shutdownNow();
    }
}
