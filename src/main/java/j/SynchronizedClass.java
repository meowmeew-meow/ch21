package j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SynchronizedClass implements Runnable{
    private int i ;
    private volatile double d;
    SynchronizedClass(int i, double d){
        this.i=i;
        this.d=d;
    }
    public synchronized void f(){
        ++i;
        Thread.yield();
        ++i;
        ++d;
        Thread.yield();
        ++d;
    }

    public synchronized int getI() {
        return i;
    }

    public synchronized double getD() {
        return d;
    }
    @Override
    public void run() {
        f();
        System.out.println(getI());
        System.out.println(getD());
    }
    public static void main(String[] args){
        ExecutorService exec = Executors.newCachedThreadPool();
        int valueI=12345;
        double valueD=1234567;
        for(int i=0;i<3;i++){
            exec.submit(new SynchronizedClass(valueI++, valueD++));
        }
        exec.shutdown();
    }
}
