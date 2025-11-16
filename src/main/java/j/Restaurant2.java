package j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//971 27
class Meal2 {
    private final int orderNum;

    public Meal2(int orderNum) {
        this.orderNum = orderNum;
    }

    public String toString() {
        return "Meal " + orderNum;
    }
}

class WaitPerson2 implements Runnable {
    private Restaurant2 restaurant;

    public WaitPerson2(Restaurant2 r) {
        restaurant = r;
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                restaurant.lock.lock();
                while (restaurant.meal == null) {
                    restaurant.condition.await();
                }
                restaurant.lock.unlock();
                System.out.println("Waitperson got " + restaurant.meal);
                restaurant.lock.lock();
                restaurant.meal = null;
                restaurant.condition.signalAll();
                restaurant.lock.unlock();
            }
        } catch (InterruptedException e) {
            System.out.println("WaitPerson interrupted");
        }
    }
}

class Chef2 implements Runnable {
    private Restaurant2 restaurant;
    private int count = 0;

    public Chef2(Restaurant2 r) {
        restaurant = r;
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                restaurant.lock.lock();
                while (restaurant.meal != null)
                    restaurant.condition.await();
                restaurant.lock.unlock();
                if (++count == 10) {
                    System.out.println("Out of food, closing");
                    restaurant.exec.shutdownNow();
                }
                System.out.print("Order up! ");
                restaurant.lock.lock();
                restaurant.meal = new Meal2(count);
                restaurant.condition.signalAll();
                restaurant.lock.unlock();
                TimeUnit.MILLISECONDS.sleep(100);
            }
        } catch (InterruptedException e) {
            System.out.println("Chef interrupted");
        }
    }
}


public class Restaurant2 {
    Meal2 meal;
    Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();
    ExecutorService exec = Executors.newCachedThreadPool();
    WaitPerson2 waitPerson = new WaitPerson2(this);
    Chef2 chef = new Chef2(this);

    public Restaurant2() {
        exec.execute(chef);
        exec.execute(waitPerson);
    }

    public static void main(String[] args) {
        new Restaurant2();
    }
}
