package com.zxg.graph.exception;

/**
 * Created by zhou1 on 2019/5/18.
 */
public class UnreachableException extends Exception {
    public UnreachableException(){
        super("NO SUCH ROUTE");
    }
}
