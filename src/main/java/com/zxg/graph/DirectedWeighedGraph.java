package com.zxg.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

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
                nodeAdjacentEdges.add(edgeObjct);
                adj.put(edgeObjct.getFrom(),nodeAdjacentEdges);
            }catch (Exception e){
                System.err.printf("%s parse error %s, skip!!!\n",edgeExpress,e.getMessage());
                continue;
            }
        }
    }

    public static void main(String[] args){
        String input = "AB5, BC4, CD8, DC8, DE6, AD5, CE2, EB3, AE7";
        DirectedWeighedGraph graph = new DirectedWeighedGraph(input);
        for(Map.Entry<String,HashSet<DirectedWeighedEdge>> entry: graph.adj.entrySet()){
            System.err.println(entry.getKey());
            System.err.println(entry.getValue());
        }
    }

}
