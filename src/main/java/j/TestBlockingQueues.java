package j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.*;
//973 28
class LiftOffRunner implements Runnable {
    private BlockingQueue<LiftOff> rockets;

    public LiftOffRunner(BlockingQueue<LiftOff> queue) {
        rockets = queue;
    }

    public void add(LiftOff lo) {
        try {
            rockets.put(lo);
        } catch (InterruptedException e) {
            System.out.println("Interrupted during put()");
        }
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                LiftOff rocket = rockets.take();
                rocket.run(); // Использовать этот поток
            }
        } catch (InterruptedException e) {
            System.out.println("Waking from take()");
        }
        System.out.println("Exiting LiftOffRunner");
    }
}

class AddInQueue implements Runnable{
    private void getkey() {
        try {
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void getkey(String message) {
        System.out.println(message);
        getkey();
    }
    public void test (String msg, BlockingQueue <LiftOff> queue) {
        System.out.println(msg);
        LiftOffRunner runner=new LiftOffRunner(queue);
        Thread t =new Thread(runner);
        t.start();
        for (int i = 0; i < 5; i++)
            runner.add(new LiftOff(5));
        getkey("Press 'Enter' (" + msg + ")");
        t.interrupt();
        System.out.println("Finished " + msg + " test");
    }
    @Override
    public void run() {
        test("LinkedBlockingQueue", // Неограниченный размер
                new LinkedBlockingQueue<LiftOff>());
        test("ArrayBlockingQueue", // Фиксированный размер
                new ArrayBlockingQueue<LiftOff>(3));
        test("SynchronousQueue", // Размер 1
                new SynchronousQueue<LiftOff>());
    }
}

public class TestBlockingQueues {
    public static void main(String[] args) {
        AddInQueue a = new AddInQueue();
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(a);
        exec.shutdownNow();
    }
}

