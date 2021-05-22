package org.insa.graphs.model;

public class Label implements Comparable<Label> {

    protected final Node node;
    protected boolean mark;
    protected double cost;
    protected int fatherId;

    /* Constructeur */
    public Label(Node node, boolean mark, double cost, int fatherId) {
        this.node = node;
        this.mark = mark;
        this.cost = cost;
        this.fatherId = fatherId;
    }


    /**
     * ALL GETTERS
     */
    public Node getNode() {
        return this.node;
    }

    public double getCost() {
        return cost;
    }

    public int getFatherId() {
        return this.fatherId;
    }

    public boolean isMark() {
        return mark;
    }

    public double getTotalCost() {
        return this.cost;
    }

    public double getEstimateCost() {
        return 0;
    }

    /**
     * ALL SETTERS
     */

    public void setMark(boolean mark) {
        this.mark = mark;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setFatherId(int fatherId) {
        this.fatherId = fatherId;
    }


    @Override
    public int compareTo(Label arg0) {
        return Double.compare(this.getTotalCost(), arg0.getTotalCost());
    }
}