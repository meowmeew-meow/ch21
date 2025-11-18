package j;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.Random;
import java.util.concurrent.*;

//977 30
class Sender implements Runnable {
    private Random rand = new Random(47);
//    private PipedWriter out = new PipedWriter();
    private BlockingQueue out = new LinkedBlockingQueue();

    public BlockingQueue getBlockingQueue() {
        return out;
    }

    public void run() {
        try {
            while (true)
                for (char c = 'A'; c <= 'z'; c++) {
                    out.put(c);
                    TimeUnit.MILLISECONDS.sleep(rand.nextInt(500));
                }
        } catch (InterruptedException e) {
            System.out.println(e + " Sender sleep interrupted");
        }
    }
}

class Receiver implements Runnable {
//    private PipedReader in;
    private BlockingQueue in;

    public Receiver(Sender sender) throws InterruptedException {
        in=sender.getBlockingQueue();
    }

    public void run() {
        try {
            while (true) {
// Блокируется до появления символов:
                System.out.print("Read: " + (char) in.take() + ", ");
            }
        } catch (InterruptedException e) {
            System.out.println(e + " Receiver read exception");
        }
    }
}

public class PipedIO {
    public static void main(String[] args) throws Exception {
        Sender sender = new Sender();
        Receiver receiver = new Receiver(sender);
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(sender);
        exec.execute(receiver);
        TimeUnit.SECONDS.sleep(4);
        exec.shutdownNow();
    }
}
