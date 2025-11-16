package j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
//968 24
class Product{
    private int number;
    Product(int id){
        number=id;
    }
    public String toString(){
        return "product "+number;
    }
}
class Producer implements Runnable{
    ProducerConsumer pc;
    private static int count=0;
    public Producer(ProducerConsumer p){
        pc=p;
    }
    public void run(){
        try {
            while (!Thread.interrupted()) {
                synchronized (this){
                    while(pc.prod!=null){
                        wait();
                    }
                }
                if(++count==10){
                    System.out.println("Closing");
                    pc.exec.shutdownNow();
                }
                System.out.println("New product");
                synchronized (pc.c){
                    pc.prod=new Product(count);
                    pc.c.notifyAll();
                }
            }
        }catch (InterruptedException e){
            System.out.println("Interrupted");
        }
    }
}
class Consumer implements Runnable{
    ProducerConsumer pc;
    Consumer(ProducerConsumer p){
        pc=p;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()){
                synchronized (this){
                    while(pc.prod==null){
                        wait();
                    }
                }
                System.out.println("Consumer got "+pc.prod);
                synchronized (pc.p){
                    pc.prod=null;
                    pc.p.notifyAll();
                }
            }
        }catch (InterruptedException e){
            System.out.println("Interrupted");
        }
    }
}

public class ProducerConsumer {
    Product prod;
    ExecutorService exec = Executors.newCachedThreadPool();
    Producer p = new Producer(this);
    Consumer c = new Consumer(this);
    public ProducerConsumer(){
        exec.execute(p);
        exec.execute(c);
    }
    public static void main(String[] args){
        new ProducerConsumer();
    }
}
