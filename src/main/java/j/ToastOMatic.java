package j;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

class Toast {
    public enum Status {DRY, PEANUT_BUTTER, JELLY, SANDWICH}

    private Status status = Status.DRY;
    private final int id;

    public Toast(int idn) {
        id = idn;
    }

    public void peanutButter() {
        status = Status.PEANUT_BUTTER;
    }

    public void jelly() {
        status = Status.JELLY;
    }

    public void sandwich() {
        status = Status.SANDWICH;
    }

    public Status getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public String toString() {
        return "Toast" + id + ": " + status;
    }
}

class ToastQueue extends LinkedBlockingQueue<Toast> {
}

class Toaster implements Runnable {
    private ToastQueue toasts;
    private int count = 0;
    private Random rand = new Random();

    public Toaster(ToastQueue tq) {
        toasts = tq;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                TimeUnit.MICROSECONDS.sleep(100 + rand.nextInt(500));
                Toast t = new Toast(count++);
                System.out.println(t);
                toasts.put(t);
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
        System.out.println("Toaster off");
    }
}

class Butterer implements Runnable {
    private ToastQueue dryToasts, butteredToast;

    public Butterer(ToastQueue dryToasts, ToastQueue butteredToast) {
        this.dryToasts = dryToasts;
        this.butteredToast = butteredToast;
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                Toast t = dryToasts.take();
                t.peanutButter();
                System.out.println(t);
                butteredToast.put(t);
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
    }
}

class Jeller implements Runnable {
    private ToastQueue dryToasts, jelledToast;

    public Jeller(ToastQueue dryToasts, ToastQueue jelledToast) {
        this.dryToasts = dryToasts;
        this.jelledToast = jelledToast;
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                Toast t = dryToasts.take();
                t.jelly();
                System.out.println(t);
                jelledToast.put(t);
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
    }
}

class ToastButterJelly extends Toast {
    private Toast butter;
    private Toast jelly;

    public ToastButterJelly(Toast butter, Toast jelly) {
        super(butter.getId());
        this.butter = butter;
        this.jelly = jelly;
    }

    public synchronized boolean isButtered() {
        while (!Thread.interrupted()) {
            if (butter != null) {
                return true;
            }
        }
        return false;
    }

    public synchronized boolean isJelled() {
        while (!Thread.interrupted()) {
            if (jelly != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Sandwich " + super.getId();
    }
}

class Sandwich implements Runnable {
    private ToastQueue butteredToast, jelledToast, finishedToast;

    public Sandwich(ToastQueue butteredToast, ToastQueue jelledToast, ToastQueue finishedToast) {
        this.butteredToast = butteredToast;
        this.jelledToast = jelledToast;
        this.finishedToast = finishedToast;
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                Toast t = butteredToast.take();
                Toast t2 = jelledToast.take();
                ToastButterJelly toast = new ToastButterJelly(t, t2);
                toast.sandwich();
                System.out.println("sandwich with peanut butter "+t.getId() +" and jelly " + t2.getId());
                finishedToast.put(toast);
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
    }
}

class Eater implements Runnable {
    private ToastQueue finishedQueue;
    private int counter = 0;

    public Eater(ToastQueue finished) {
        finishedQueue = finished;
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                Toast t = finishedQueue.take();
                System.out.println("Chomp " + counter++);
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
    }
}

public class ToastOMatic {
    public static void main(String[] args) throws InterruptedException {
        ToastQueue
                dryQueue = new ToastQueue(),
                butteredToast = new ToastQueue(),
                jelledToast = new ToastQueue(),
                finishedQueue = new ToastQueue();
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new Toaster(dryQueue));
        exec.execute(new Sandwich(butteredToast, jelledToast, finishedQueue));
        exec.execute(new Butterer(dryQueue, butteredToast));
        exec.execute(new Jeller(dryQueue, jelledToast));
        exec.execute(new Eater(finishedQueue));
        TimeUnit.SECONDS.sleep(1);
        exec.shutdownNow();
    }
}
