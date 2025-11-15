package j;
import java.util.concurrent.TimeUnit;

//952 18
public class Interrupting {
    public void f() {
        try {
            TimeUnit.SECONDS.sleep(20);
        } catch (InterruptedException e) {
            System.out.println("Interrupted Exception");
        }
        System.out.println("Exiting");
    }

    public Thread run(){
        return new Thread(){
            @Override
            public void run() {
                f();
            }
        };
    }
    public static void main(String[] args) throws InterruptedException {
        Interrupting i=new Interrupting();
        Thread t=i.run();
        t.start();
        TimeUnit.SECONDS.sleep(10);
        t.interrupt();
    }
}
