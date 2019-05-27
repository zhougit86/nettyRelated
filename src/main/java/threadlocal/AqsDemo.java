package threadlocal;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by zhou1 on 2019/5/21.
 */
public class AqsDemo {
    static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args){
        lock.lock();


    }
}
