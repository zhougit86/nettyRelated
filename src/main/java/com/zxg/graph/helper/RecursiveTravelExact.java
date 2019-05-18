package com.zxg.graph.helper;

import com.zxg.graph.DirectedWeighedEdge;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Created by zhou1 on 2019/5/17.
 * <p>
 * for case 6
 * <p>
 * 递归发现两点之间可能的路径
 */
public class RecursiveTravelExact {
    private HashMap<String, HashSet<DirectedWeighedEdge>> adj;
    private LinkedList<LinkedList<String>> result = new LinkedList<>();
    private LinkedList<String> tempStask = new LinkedList<>();
    private int maxStop;
    private String end;

    public RecursiveTravelExact(HashMap<String, HashSet<DirectedWeighedEdge>> input, String start, String end, int maxStop) {
        this.adj = input;
        this.maxStop = maxStop;
        this.end = end;
        traverse(start);
    }

    public void traverse(String newStart) {

        tempStask.addLast(newStart);

        //如果不是刚加入栈，且和end相同，就返回
        if (tempStask.size() == maxStop + 1 && newStart.equals(end)) {
            result.addLast((LinkedList<String>) tempStask.clone());
            return;
        }
        //如果已经达到最大
        if (tempStask.size() >= (maxStop + 1)) {
            return;
        }
        for (DirectedWeighedEdge edge : adj.get(newStart)) {
            traverse(edge.getTo());
            tempStask.removeLast();
        }
    }

    public LinkedList<LinkedList<String>> getPossibleRoute() {
        return result;
    }
}
