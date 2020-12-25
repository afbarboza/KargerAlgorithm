package com.learning;

public class EdgeType
{
    private String nodeU;
    private String nodeV;

    public EdgeType(String nodeU, String nodeV) {
        this.nodeU = nodeU;
        this.nodeV = nodeV;
    }

    public String getNodeU() {
        return this.nodeU;
    }

    public String getNodeV() {
        return this.nodeV;
    }

}
