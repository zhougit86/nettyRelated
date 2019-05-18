package com.zxg;

import com.zxg.graph.DirectedWeighedEdge;
import com.zxg.graph.DirectedWeighedGraph;

import java.util.HashSet;
import java.util.Map;

/**
 * Created by zhou1 on 2019/5/17.
 */
public class StartUp {
    public static void main(String[] args){
        String input = "AB5, BC4, CD8, DC8, DE6, AD5, CE2, EB3, AE7";
        DirectedWeighedGraph graph = new DirectedWeighedGraph(input);
        for (Map.Entry<String, HashSet<DirectedWeighedEdge>> entry : graph.getAdj().entrySet()) {


        }



//        try{
//            System.err.println(graph.chainReachability("A-B-C"));
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        try{
//            System.err.println(graph.chainReachability("A-D"));
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        try{
//            System.err.println(graph.chainReachability("A-D-C"));
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        try{
//            System.err.println(graph.chainReachability("A-E-B-C-D"));
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        try{
//            System.err.println(graph.chainReachability("A-E-D"));
//        }catch (Exception e){
//            e.printStackTrace();
//        }



        //#6
        System.err.println(graph.pathMaxStops("C", "C", 3));
        //#7
        System.err.println(graph.pathExactStops("A", "C", 4));

        //#8
        try{
            System.err.println(graph.shortestPath("A", "C"));
        }catch (Exception e){
            e.printStackTrace();
        }

        //#9
        try{
            System.err.println(graph.shortestCycle("C"));
        }catch (Exception e){
            e.printStackTrace();
        }

        //#10
        System.err.println(graph.pathMaxDistance("C", "C", 30));
    }
}
