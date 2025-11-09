package j;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Counter{
    private int count=0;
    public synchronized int increment(){
        int temp=count;
        return count=++temp;
    }
    public synchronized int value(){
        return count;
    }
}
class Sensor implements Runnable{
    private static Counter counter=new Counter();
    static List<Sensor> sensors= new ArrayList<>();
    private int number=0;
    private final int id;
    private static boolean cansel=true;
    Sensor(int id){
        this.id=id;
        sensors.add(this);
    }
    @Override
    public void run() {
        while (cansel) {
        synchronized (this) {
            ++number;
        }
            System.out.println(this + " Total: " + counter.increment());
        }
    }
    public static void cansel(){
        cansel=false;
    }
    public synchronized int getNumber(){return number;}
    public String toString(){
        return "Sensor "+id+" : "+getNumber();
    }
    public static int getTotalValue(){
        return counter.value();
    }
    public static int sumSensors(){
        int sum=0;
        for (Sensor sensor:sensors){
            sum+=sensor.getNumber();
        }
        return sum;
    }
}
public class RadiationCounter {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i=0;i<3;i++){
            exec.execute(new Sensor(i));
        }
        TimeUnit.SECONDS.sleep(3);
        Sensor.cansel();
        exec.shutdown();
        if(!exec.awaitTermination(250, TimeUnit.MILLISECONDS))
        System.out.println("Some tasks were not terminated!");
        System.out.println("Total: "+Sensor.getTotalValue());
        System.out.println("Sensors sum: "+ Sensor.sumSensors());
    }
}
