package j;
//896 1
public class MainThread implements Runnable{
    private static int counter = 0;
    private final int id = counter++;
    MainThread(){
        System.out.println("Constructor start "+id);
        System.out.println("Constructor finish "+ id);
    }
    @Override
    public void run() {
        System.out.println("Massage "+id);
        Thread.yield();
        System.out.println("Massage2 "+id);
        Thread.yield();
        System.out.println("Massage3 "+id);
        Thread.yield();
    }
    public static void main(String[] args){
        Thread thread = new Thread(new MainThread());
        Thread thread1 = new Thread(new MainThread());
        thread.start();
        thread1.start();
    }
}
