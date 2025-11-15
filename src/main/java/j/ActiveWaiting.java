package j;
//962 22
import java.util.concurrent.TimeUnit;

public class ActiveWaiting {
    static boolean b;
    public synchronized Thread task1() {
        return new Thread() {
            public synchronized void run () {
                try {
                    System.out.println("Task1");
                    TimeUnit.SECONDS.sleep(10);
                    b = true;
                    System.out.println("Task1 complete");
                } catch (InterruptedException e) {
                    System.out.println("Interrupted");
                }
            }
        };
    }
    public synchronized Thread task2(){
       return new Thread(){
            public synchronized void run() {
                System.out.println("Task2");
                Thread.yield();
                while(b==true){
                    b=false;
                    System.out.println("the flag has been changed");
                }
            }
        };
    }
    public static void main(String[] args) throws InterruptedException {
        ActiveWaiting aw= new ActiveWaiting();
        Thread t1=aw.task1();
        Thread t2=aw.task2();
        t2.start();
        t1.start();
        TimeUnit.SECONDS.sleep(60);
        t1.interrupt();
        t2.interrupt();
    }
}
