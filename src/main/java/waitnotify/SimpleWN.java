package waitnotify;

/**
 * Created by zhou1 on 2019/5/22.
 */
public class SimpleWN {
    public static final Object lockObj = new Object();

    public static void main(String[] args){
        Thread waitThread = new Thread(){
            public void run(){
                synchronized (lockObj){
                    System.err.println("a start");
                    try{
                        lockObj.wait();
                        System.err.println("a finish waiting");
                    }catch (InterruptedException e){
                        System.err.println("a got exception");
                    }
                }
                System.err.println("a finish");
            }
        };

        Thread notifyThread = new Thread(){
            public void run(){
                try {
                    Thread.sleep(1000L);
                }catch (Exception e){

                }

                synchronized (lockObj){
                    System.err.println("b start");

                    lockObj.notify();
                    System.err.println("b finish notify");
                }
                System.err.println("b finish");
            }
        };

        waitThread.start();
        notifyThread.start();
    }
}
