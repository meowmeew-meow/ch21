package j;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
//941 16
public class CriticalSections2 {
    int i;
    public void f(){
        Lock lock = new ReentrantLock();
        lock.lock();
        try {
            i++;
            for (int j = 0; j < 5; j++) {
                System.out.println(i);
            }
        }finally {
            lock.unlock();
        }
    }
    public void g(){
        Lock lock = new ReentrantLock();
        lock.lock();
        try {
            i--;
            for (int j = 0; j < 5; j++) {
                System.out.println(i);
            }
        }finally {
            lock.unlock();
        }
    }
    public void t(){
        Lock lock = new ReentrantLock();
        lock.lock();
        try {
            i+=10;
            for (int j = 0; j < 5; j++) {
                System.out.println(i);
            }
        }finally {
            lock.unlock();
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

