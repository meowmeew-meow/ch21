package j;
//896 2
public class Fibonacci implements Runnable, Generator<Integer> {
    private int count = 0;
    Fibonacci(int count){
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
        for (int i = 0; i < 18; i++){
            Thread t = new Thread(new Fibonacci(i));
            t.start();
        }
    }
}
