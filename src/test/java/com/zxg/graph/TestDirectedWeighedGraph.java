package com.zxg.graph;

import com.zxg.graph.exception.UnreachableException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by zhou1 on 2019/5/18.
 */
public class TestDirectedWeighedGraph {
    @Test
    public void testNodeNumberRight(){
        String input = "AB1,CD2,EF3";
        DirectedWeighedGraph graph = new DirectedWeighedGraph(input);

        Assert.assertEquals(6,graph.getAdj().size());
    }

    @Test
    public void testEdgeNumberRight(){
        String input = "AB1,AC3,AF9,CD2,EF3";
        DirectedWeighedGraph graph = new DirectedWeighedGraph(input);

        Assert.assertEquals(6,graph.getAdj().size());
        Assert.assertEquals(3,graph.getAdj().get("A").size());
        Assert.assertEquals(0,graph.getAdj().get("B").size());
    }

    @Test(expected = UnreachableException.class)
    public void testChainReachability() throws Exception{
        String input = "BA2, BC3";
        DirectedWeighedGraph graph = new DirectedWeighedGraph(input);

        graph.chainReachability("A-C-B");
    }
}
