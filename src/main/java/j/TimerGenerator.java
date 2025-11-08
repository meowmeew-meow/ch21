package j;

import java.util.Timer;
import java.util.TimerTask;

//935 14
public class TimerGenerator {
    Timer t = new Timer();
    public void newTimer(String s, long timeout){
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println(s);
            }
        }, timeout);
    }
    public void exit(long timeout){
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                System.exit(0);
            }
        },timeout);
    }
    public static void main(String[] args){
        TimerGenerator tg = new TimerGenerator();
        long timeout=1000;
        for (int i=0;i<20;i++){
            tg.newTimer(String.valueOf(i), timeout);
            timeout+=1000;
        }
        tg.exit(timeout+1000);
    }
}
