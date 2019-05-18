package com.zxg.graph;

import com.zxg.graph.exception.InvalidInputException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.fail;


/**
 * Created by zhou1 on 2019/5/18.
 */
public class TestDirectedWeightedEdge {
    @Test
    public void testInvalidExpress(){
        String invalidInput1 = "ABC10";
        String invalidInput2 = "AH-10";
        String invalidInput3 = "AB 10";

        try{
            DirectedWeighedEdge edge1 = new DirectedWeighedEdge(invalidInput1);
            fail("uncaught failure");

        }catch (Exception e){
            Assert.assertTrue(e instanceof InvalidInputException);
        }

        try{
            DirectedWeighedEdge edge2 = new DirectedWeighedEdge(invalidInput2);
            fail("uncaught failure");

        }catch (Exception e){
            Assert.assertTrue(e instanceof InvalidInputException);
        }

        try{
            DirectedWeighedEdge edge3 = new DirectedWeighedEdge(invalidInput3);
            fail("uncaught failure");

        }catch (Exception e){
            Assert.assertTrue(e instanceof InvalidInputException);
        }

    }

    @Test
    public void testEqual(){
        String input1 = "AB100";
        String input2 = "AB10";

        DirectedWeighedEdge edge1= null,edge2 = null;

        try{
            edge1 = new DirectedWeighedEdge(input1);
        }catch (Exception e){

        }

        try{
            edge2 = new DirectedWeighedEdge(input2);
        }catch (Exception e){

        }

        Assert.assertEquals( edge1,edge2);
    }

    @Test
    public void testCmp(){
        String input1 = "AB100";
        String input2 = "AB10";

        DirectedWeighedEdge edge1= null,edge2 = null;

        try{
            edge1 = new DirectedWeighedEdge(input1);
        }catch (Exception e){

        }

        try{
            edge2 = new DirectedWeighedEdge(input2);
        }catch (Exception e){

        }

        Assert.assertTrue( edge1.compareTo(edge2) > 0);
    }
}
