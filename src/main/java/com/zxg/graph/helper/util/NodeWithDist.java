package com.zxg.graph.helper.util;

import java.util.Objects;

public class NodeWithDist implements Comparable<NodeWithDist> {
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
        NodeWithDist s = (NodeWithDist) other;
        return Objects.equals(this.getNodeName(), s.getNodeName());
    }

    @Override
    public int compareTo(NodeWithDist o) {
        return this.getDistFromStart() - o.getDistFromStart();
    }
}