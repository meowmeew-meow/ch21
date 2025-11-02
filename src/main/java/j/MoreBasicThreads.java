package j;

import java.util.concurrent.TimeUnit;

public class MoreBasicThreads {
    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(new Fibonacci(i));
            thread.setDaemon(true);
            thread.start();
        }
        System.out.println("Waiting for calculate");
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

