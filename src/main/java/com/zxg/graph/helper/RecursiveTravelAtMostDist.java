package com.zxg.graph.helper;

import com.zxg.graph.DirectedWeighedEdge;
import com.zxg.graph.helper.util.NodeWithDist;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by zhou1 on 2019/5/17.
 * <p>
 * for case 6
 * <p>
 * 递归发现两点之间可能的路径
 */
public class RecursiveTravelAtMostDist {
    private HashMap<String, HashSet<DirectedWeighedEdge>> adj;
    private LinkedList<List<String>> result = new LinkedList<>();
    private LinkedList<NodeWithDist> tempStask = new LinkedList<>();
    private int maxDistance;
    private String end;

    public RecursiveTravelAtMostDist(HashMap<String, HashSet<DirectedWeighedEdge>> input, String start, String end, int maxDistance) {
        this.adj = input;
        this.maxDistance = maxDistance;
        this.end = end;
        traverse(new NodeWithDist(start, 0));
    }

    public static int pathDistance(LinkedList<NodeWithDist> pathList){
        return pathList.stream()
                .map(x -> x.getDistFromStart())
                .reduce(Integer::sum).get();
    }

    public void traverse(NodeWithDist nodeWithDist ) {

        tempStask.addLast(nodeWithDist);

        //如果不是刚加入栈，且和end相同，就返回
        if (tempStask.size() != 1 && pathDistance(tempStask) < maxDistance &&
                nodeWithDist.getNodeName().equals(end)) {
            result.addLast(tempStask.stream()
                    .map(x->x.getNodeName())
                    .collect(Collectors.toList())
            );
        }
        //如果已经达到最大
        if (tempStask.size() >= maxDistance) {
            return;
        }
        for (DirectedWeighedEdge edge : adj.get(nodeWithDist.getNodeName())) {
            traverse(new NodeWithDist(edge.getTo(),edge.getWeight()));
            tempStask.removeLast();
        }
    }

    public LinkedList<List<String>> getPossibleRoute() {
        return result;
    }
}
