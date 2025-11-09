package j;

//941 15
public class CriticalSections{
    private Object syncObject=new Object();
    private Object syncObject2 = new Object();
    int i;
    public void f(){
        synchronized (this) {
            i++;
            for (int j = 0; j < 5; j++) {
                System.out.println(i);
            }
        }
    }
    public void g(){
        synchronized (syncObject){
            i--;
            for (int j = 0; j < 5; j++) {
                System.out.println(i);
            }
        }
    }
    public void t(){
        synchronized (syncObject2){
            i+=10;
            for (int j = 0; j < 5; j++) {
                System.out.println(i);
            }
        }
    }

    public static void main(String[] args){
        CriticalSections cs = new CriticalSections();
        new Thread(){
            public void run(){
                cs.f();
            }
        }.start();
        new Thread(){
            public void run(){
                cs.g();
            }
        }.start();
        cs.t();
    }
}
