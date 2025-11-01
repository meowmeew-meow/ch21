package j;

import java.util.ArrayList;
import java.util.concurrent.*;
//900 5

public class Fibonacci2 implements Callable<Integer>, Generator<Integer> {
    private int count = 0;
    Fibonacci2(int count){
        this.count=count;
    }
    public Integer next() {
        return fib(count);
    }

    private int fib(int n) {
        if (n < 2) return 1;
        return fib(n - 2) + fib(n - 1);
    }

    public void run() {
        System.out.print(next()+" ");
    }

    @Override
    public Integer call() throws Exception {
        return next();
    }

    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        ArrayList<Future<Integer>> result= new ArrayList<>();
        for (int i = 0; i < 18; i++){
            result.add(exec.submit(new Fibonacci2(i)));
        }
        int sum=0;
        for (Future<Integer> f: result){
            try {
                sum+=f.get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }finally {
                exec.shutdown();
            }
        }
        System.out.println(sum);
    }


}
