package org.insa.graphs.model;

public class Label implements Comparable<Label> {

    private int nodeId;
    private boolean mark;
    private double cost;
    private int fatherId;

    /* Constructeur */
    public Label(int nodeId, boolean mark, double cost, int fatherId) {
        this.nodeId = nodeId;
        this.mark = mark;
        this.cost = cost;
        this.fatherId = fatherId;
    }

    public int getNodeId() {
        return this.nodeId;
    }

    public double getCost() {
        return cost;
    }

    public boolean isMark() {
        return mark;
    }

    public void setMark(boolean mark) {
        this.mark = mark;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getFatherId() {
        return this.fatherId;
    }

    public void setFatherId(int fatherId) {
        this.fatherId = fatherId;
    }

    @Override
    public int compareTo(Label arg0) {
        return Double.compare(this.cost, arg0.cost);
    }
}