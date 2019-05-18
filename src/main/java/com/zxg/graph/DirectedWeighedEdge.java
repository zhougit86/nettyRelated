package com.zxg.graph;

import com.zxg.graph.exception.InvalidInputException;

import java.util.Objects;

/**
 * Created by zhou1 on 2019/5/17.
 * <p>
 * 代表有权重的，有向边
 */
public class DirectedWeighedEdge implements Comparable<DirectedWeighedEdge> {
    private String from;
    private String to;
    private int weight;

    //根据表达式生成一条边
    public DirectedWeighedEdge(String edgeExpress) throws Exception {

        //todo: the question is unclear, as mentioned only A-D used, but E also exists?
        boolean isMatch = edgeExpress.matches("^[A-Z]{2}\\d+$");
        if (!isMatch) {
            throw new InvalidInputException(edgeExpress);
        }

        this.from = edgeExpress.substring(0, 1);
        this.to = edgeExpress.substring(1, 2);
        this.weight = Integer.valueOf(edgeExpress.substring(2));
    }


    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return String.format("%s to %s weight %d", from, to, weight);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((from == null) ? 0 : from.hashCode());
        result = prime * result + ((to == null) ? 0 : to.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        // must return false if the explicit parameter is null
        if (other == null) {
            return false;
        }

        // if the classes don ' t match , they can ' t be equal
        if (other.getClass() != this.getClass()) {
            return false;
        }

        // now we know other is a non - null Student
        DirectedWeighedEdge s = (DirectedWeighedEdge) other;
        return Objects.equals(this.from, s.from) && Objects.equals(this.to, s.to);
    }

    @Override
    public int compareTo(DirectedWeighedEdge o) {
        return this.getWeight() - o.getWeight();
    }

}
