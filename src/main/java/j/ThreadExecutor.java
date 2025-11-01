package j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadExecutor implements Runnable, Generator<Integer>  {
    private int count = 0;
    ThreadExecutor(int count){
        this.count=count;
    }
    public Integer next() {
        return fib(count);
    }

    private int fib(int n) {
        if (n < 2) return 1;
        return fib(n - 2) + fib(n - 1);
    }

    @Override
    public void run() {
        System.out.print(next()+" ");
    }

    public static void main(String[] args) {
        ExecutorService exs = Executors.newCachedThreadPool();
        for (int i = 0; i < 18; i++){
            exs.execute(new ThreadExecutor(i));
        }
        exs.shutdown();
    }
}
