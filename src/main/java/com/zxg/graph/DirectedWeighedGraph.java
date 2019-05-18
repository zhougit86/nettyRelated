package com.zxg.graph;

import com.zxg.graph.exception.UnreachableException;
import com.zxg.graph.helper.RecursiveTravelAtMost;
import com.zxg.graph.helper.RecursiveTravelAtMostDist;
import com.zxg.graph.helper.RecursiveTravelExact;
import com.zxg.graph.helper.ShortestPathHelper;

import java.util.*;

/**
 * Created by zhou1 on 2019/5/17.
 * <p>
 * 使用邻接表来表示带权重的有向图
 */
public class DirectedWeighedGraph {

    HashMap<String, HashSet<DirectedWeighedEdge>> adj;

    public DirectedWeighedGraph(String input) {
        adj = new HashMap<>();

        String[] inputList = input.split(",");
        for (String edgeExpress : inputList) {
            String edgeTrimed = edgeExpress.trim();
            try {
                DirectedWeighedEdge edgeObjct = new DirectedWeighedEdge(edgeTrimed);

                HashSet<DirectedWeighedEdge> nodeAdjacentEdges = adj.get(edgeObjct.getFrom());
                if (nodeAdjacentEdges == null) {
                    nodeAdjacentEdges = new HashSet<>();
                }

                //todo: As mentioned in question:
                // A given route will never appear more than once
                //no need to check the duplicate add
                nodeAdjacentEdges.add(edgeObjct);
                adj.put(edgeObjct.getFrom(), nodeAdjacentEdges);

                HashSet<DirectedWeighedEdge> nodeAdjacentEdgeToNode = adj.get(edgeObjct.getTo());
                if (nodeAdjacentEdgeToNode == null) {
                    nodeAdjacentEdgeToNode = new HashSet<>();
                    adj.put(edgeObjct.getTo(), nodeAdjacentEdgeToNode);
                }
            } catch (Exception e) {
                System.err.printf("%s parse error %s, skip!!!\n", edgeExpress, e.getMessage());
                continue;
            }
        }
    }

    public HashMap<String, HashSet<DirectedWeighedEdge>> getAdj() {
        return adj;
    }

    //for case 1-5
    public int chainReachability(String nodeChain) throws Exception {
        int totalDistance = 0;
        String[] nodeList = nodeChain.split("-");
        for (int i = 0, j = 1; j < nodeList.length; j++, i++) {
            String srcNode = nodeList[i].trim();
            String dstNode = nodeList[j].trim();

            HashSet<DirectedWeighedEdge> set = adj.get(srcNode);
            boolean noMatch = true;

            for (DirectedWeighedEdge edge : adj.get(srcNode)) {
                if (edge.getTo().equals(dstNode)) {
                    noMatch = false;
                    totalDistance += edge.getWeight();
                    break;
                }
            }

            if (noMatch) {
                throw new UnreachableException();
            }
        }
        return totalDistance;
    }

    // for case 6
    public LinkedList<LinkedList<String>> pathMaxStops(String start, String end, int maxStop) {
        RecursiveTravelAtMost recursiveTravel = new RecursiveTravelAtMost(this.adj, start, end, maxStop);
        return recursiveTravel.getPossibleRoute();
    }

    // for case 7
    public LinkedList<LinkedList<String>> pathExactStops(String start, String end, int maxStop) {
        RecursiveTravelExact recursiveTravel = new RecursiveTravelExact(this.adj, start, end, maxStop);
        return recursiveTravel.getPossibleRoute();
    }

    // for case 10
    public LinkedList<List<String>> pathMaxDistance(String start, String end, int maxStop) {
        RecursiveTravelAtMostDist recursiveTravel = new RecursiveTravelAtMostDist(this.adj, start, end, maxStop);
        return recursiveTravel.getPossibleRoute();
    }

    //for case 8
    public int shortestPath(String source, String target) throws Exception {
        ShortestPathHelper shortestPathHelper = new ShortestPathHelper(source, adj);
        Integer distance = shortestPathHelper.distanceToNode(target);
        if (distance.equals(Integer.MAX_VALUE)) {
            throw new UnreachableException();
        }

        return distance;
    }

    //case #9
    public int shortestCycle(String source) throws Exception {
        Integer cycleDistance = Integer.MAX_VALUE;
        ShortestPathHelper shortestPathHelper = new ShortestPathHelper(source, adj);
        for (String lastNodeToSource : adj.keySet()) {
            //首先排除自己
            if (lastNodeToSource.equals(source)) {
                continue;
            }

            HashSet<DirectedWeighedEdge> adjEdge = adj.get(lastNodeToSource);
            for (DirectedWeighedEdge edgeBackToSelf : adjEdge) {
                if (edgeBackToSelf.getTo().equals(source)) {
                    Integer sourceToLastNodeDistance = shortestPathHelper.distanceToNode(lastNodeToSource);
                    if (!sourceToLastNodeDistance.equals(Integer.MAX_VALUE) &&
                            (edgeBackToSelf.getWeight() + sourceToLastNodeDistance) < cycleDistance) {
                        cycleDistance = edgeBackToSelf.getWeight() + sourceToLastNodeDistance;
                    }
                }
            }
        }

        if (cycleDistance.equals(Integer.MAX_VALUE)) {
            throw new UnreachableException();
        }

        return cycleDistance;
    }

}
