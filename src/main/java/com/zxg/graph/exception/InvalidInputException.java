package com.zxg.graph.exception;

/**
 * Created by zhou1 on 2019/5/18.
 */
public class InvalidInputException extends Exception {
    String input;

    public InvalidInputException(String edgeInput) {
        super("INVALID INPUT");
        input = edgeInput;
    }
}
