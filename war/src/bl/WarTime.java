package bl;

public class WarTime implements Runnable{
    private static long startTime;

    public int getTime(){
        return Math.toIntExact((System.nanoTime() - startTime)/1000000000 );
    }
    public double getExactTime(){
        double time =  (System.nanoTime() - startTime)/10000000;
        return time/100;
    }
    
    public void startTimer() {
    	WarTime.startTime = System.nanoTime();
    }
    
    @Override
    public void run() {
        WarTime.startTime = System.nanoTime();
    }

    public void killTimer() {
        Thread.currentThread().interrupt();
        return;
    }
}
