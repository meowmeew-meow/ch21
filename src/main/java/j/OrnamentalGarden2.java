package j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
//984 32
class Count2 {
    private int count = 0;
    private Random rand = new Random(47);

    public synchronized int increment() {
        int temp = count;
        if (rand.nextBoolean()) // Уступает управление
            Thread.yield(); // в половине случаев
        return (count = ++temp);
    }

    public synchronized int value() {
        return count;
    }
}

class Entrance2 implements Runnable {
    private static Count2 count = new Count2();
    private static List<Entrance2> entrances =
            new ArrayList<>();
    private int number = 0;
    private final int id;
    private static volatile boolean canceled = false;

    public static void cancel() {
        canceled = true;
    }

    public Entrance2(int id) {
        this.id = id;
        entrances.add(this);
    }
    public void run() {
        while (!canceled) {
            try {
                synchronized (this) {
                    ++number;
                }
                System.out.println(this + " Total: " + count.increment());
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {

                    System.out.println("sleep interrupted");
                }
            }finally {
                cancel();
            }
        }
        System.out.println("Stopping" + this);
    }
    public synchronized int getValue() {
        return number;
    }

    public String toString() {
        return "Entrance " + id + ": " + getValue();
    }

    public static int getTotalCount() {
        return count.value();
    }

    public static int sumEntrances() {
        int sum = 0;
        for (Entrance2 entrance : entrances)
            sum += entrance.getValue();
        return sum;
    }
}

public class OrnamentalGarden2 {
    public static void main (String[] args) throws Exception {
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            exec.execute(new Entrance2(i));
        }
        TimeUnit.SECONDS.sleep(3);
        Entrance.cancel();
        exec.shutdown();
        if(!exec.awaitTermination(250, TimeUnit.MILLISECONDS))
        System.out.println("Some tasks were not terminated!");
        System.out.println("Total: " + Entrance2.getTotalCount());
        System.out.println("Sum of Entrances: " + Entrance2.sumEntrances());
    }
}
