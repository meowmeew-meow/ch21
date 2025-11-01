package j;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SleepingTask implements Runnable{
    Random rand = new Random();
    public void run() {
        try {
            int time=rand.nextInt(10);
            TimeUnit.MICROSECONDS.sleep(time);
            System.out.print(time+" ");
        } catch (InterruptedException e) {
            System.err.println("Interrupted");
        }
    }
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("incorrect number of values");
        } else {
            int tasks = Integer.parseInt(args[0]);
            ExecutorService exec = Executors.newCachedThreadPool();
            for (int i=0;i<tasks;i++){
                exec.execute(new SleepingTask());
            }
            exec.shutdown();
        }
    }
}
