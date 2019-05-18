package com.zxg.graph.helper;

import com.zxg.graph.DirectedWeighedEdge;
import com.zxg.graph.helper.util.NodeWithDist;

import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

/**
 * Created by zhou1 on 2019/5/18.
 */
public class ShortestPathHelper {
    private HashMap<String, HashSet<DirectedWeighedEdge>> adj;

    private HashMap<String, Integer> distTo = new HashMap<>();
    private HashMap<String, DirectedWeighedEdge> edgeTo = new HashMap<>();
    private PriorityQueue<NodeWithDist> pq = new PriorityQueue<>();
    private String start;


    public ShortestPathHelper(String start, HashMap<String, HashSet<DirectedWeighedEdge>> adjInput) {
        this.start = start;
        this.adj = adjInput;


        for (String node : this.adj.keySet()) {
            this.distTo.put(node, Integer.MAX_VALUE);
        }
        this.distTo.put(start, 0);

        this.pq.offer(new NodeWithDist(start, 0));
        while (!pq.isEmpty()) {
            relax(pq.poll());
        }
    }

    private void relax(NodeWithDist sourceNode) {
        for (DirectedWeighedEdge edge : this.adj.get(sourceNode.getNodeName())) {
            String edgeDst = edge.getTo();
            if (distTo.get(edgeDst) > distTo.get(sourceNode.getNodeName()) + edge.getWeight()) {
                int distanceToStart = distTo.get(sourceNode.getNodeName()) + edge.getWeight();
                distTo.put(edgeDst, distanceToStart);
                edgeTo.put(edgeDst, edge);

                NodeWithDist nodeToAdd = new NodeWithDist(edgeDst, edge.getWeight());
                if (pq.contains(nodeToAdd)) {
                    pq.remove(nodeToAdd);
                }
                pq.offer(nodeToAdd);
            }
        }
    }

    public int distanceToNode(String dst) {
        return this.distTo.get(dst);
    }

    public String getStart() {
        return this.start;
    }


}

