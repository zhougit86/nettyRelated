package com.zxg.graph;

import java.util.*;

/**
 * Created by zhou1 on 2019/5/17.
 *
 * 使用邻接表来表示带权重的有向图
 */
public class DirectedWeighedGraph {

    HashMap<String,HashSet<DirectedWeighedEdge>> adj;

    public DirectedWeighedGraph(String input){
        adj = new HashMap<>();

        String[] inputList = input.split(",");
        for (String edgeExpress: inputList){
            String edgeTrimed = edgeExpress.trim();
            try{
                DirectedWeighedEdge edgeObjct = new DirectedWeighedEdge(edgeTrimed);

                HashSet<DirectedWeighedEdge> nodeAdjacentEdges = adj.get(edgeObjct.getFrom());
                if (nodeAdjacentEdges == null) {
                    nodeAdjacentEdges = new HashSet<>();
                }

                //As mentioned in question:
                // A given route will never appear more than once
                //no need to check the duplicate add
                nodeAdjacentEdges.add(edgeObjct);
                adj.put(edgeObjct.getFrom(),nodeAdjacentEdges);
            }catch (Exception e){
                System.err.printf("%s parse error %s, skip!!!\n",edgeExpress,e.getMessage());
                continue;
            }
        }
    }

    class ShortestPathHelper{
        HashMap<String, Integer> distTo = new HashMap<>();
        HashMap<String, DirectedWeighedEdge> edgeTo = new HashMap<>();
        PriorityQueue<NodeWithDist> pq = new PriorityQueue<>();
        String start;
        String end;


        public ShortestPathHelper(String start, String end){
            this.start = start;
            this.end = end;

            for (String node : DirectedWeighedGraph.this.adj.keySet()){
                this.distTo.put(node,Integer.MAX_VALUE);
            }
            this.distTo.put(start,0);

            this.pq.offer(new NodeWithDist(start,0));
            while (!pq.isEmpty()){
                relax(pq.poll());
            }
        }

        private void relax(NodeWithDist sourceNode){
            for(DirectedWeighedEdge edge : DirectedWeighedGraph.this.adj.get(sourceNode.getNodeName())){
                String edgeDst = edge.getTo();
                if (distTo.get(edgeDst) > distTo.get(sourceNode.getNodeName()) + edge.getWeight()){
                    int distanceToStart = distTo.get(sourceNode.getNodeName()) + edge.getWeight();
                    distTo.put(edgeDst, distanceToStart);
                    edgeTo.put(edgeDst, edge);

                    NodeWithDist nodeToAdd = new NodeWithDist(edgeDst,edge.getWeight());
                    if(pq.contains(nodeToAdd)) {
                        pq.remove(nodeToAdd);
                    }
                    pq.offer(nodeToAdd);
                }
            }
        }

        public int distanceToNode(String dst){
            return this.distTo.get(dst);
        }
    }

    public int shortestPath(String source, String target){
        ShortestPathHelper shortestPathHelper = new ShortestPathHelper(source,target);
        return shortestPathHelper.distanceToNode(target);
    }

    public static void main(String[] args){
        String input = "AB5, BC4, CD8, DC8, DE6, AD5, CE2, EB3, AE7";
//        PriorityQueue<DirectedWeighedEdge> pq = new PriorityQueue<>();
        DirectedWeighedGraph graph = new DirectedWeighedGraph(input);
        for(Map.Entry<String,HashSet<DirectedWeighedEdge>> entry: graph.adj.entrySet()){
//            System.err.println(entry.getKey());
//            System.err.println(entry.getValue());
//            for (DirectedWeighedEdge edge : entry.getValue()){
//                pq.offer(edge);
//            }
        }
//        while (!pq.isEmpty()){
//            System.out.println(pq.poll());
//        }

        System.out.println(graph.shortestPath("A","C"));
    }

}

class NodeWithDist implements Comparable<NodeWithDist>{
    String nodeName;
    Integer distFromStart;

    public NodeWithDist(String nodeName, int distFromStart) {
        this.nodeName = nodeName;
        this.distFromStart = distFromStart;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public Integer getDistFromStart() {
        return distFromStart;
    }

    public void setDistFromStart(Integer distFromStart) {
        this.distFromStart = distFromStart;
    }

    @Override
    public boolean equals(Object other){
        if(other == this){
            return true;
        }
        // must return false if the explicit parameter is null
        if(other == null){
            return false;
        }

        // if the classes don ' t match , they can ' t be equal
        if(other.getClass()!= this.getClass()){
            return false;
        }

        // now we know other is a non - null Student
        NodeWithDist s = (NodeWithDist)other;
        return Objects.equals(this.getNodeName(), s.getNodeName());
    }

    @Override
    public int compareTo(NodeWithDist o){
        return this.getDistFromStart() - o.getDistFromStart();
    }
}
