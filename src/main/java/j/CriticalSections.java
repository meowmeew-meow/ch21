package j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//941 15
public class CriticalSections{
    int i;
    public void f(){
        synchronized (this) {
            i++;
            for (int j = 0; j < 5; j++) {
                System.out.println(i);
            }
        }
    }
    public void g(){
        synchronized (this){
            i--;
            for (int j = 0; j < 5; j++) {
                System.out.println(i);
            }
        }
    }
    public void t(){
        synchronized (this){
            i+=10;
            for (int j = 0; j < 5; j++) {
                System.out.println(i);
            }
        }
    }

    public static void main(String[] args){
        CriticalSections cs = new CriticalSections();
        new Thread(){
            public void run(){
                cs.f();
            }
        }.start();
        new Thread(){
            public void run(){
                cs.g();
            }
        }.start();
        cs.t();
    }
}
