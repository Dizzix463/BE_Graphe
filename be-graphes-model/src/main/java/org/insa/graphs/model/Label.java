package org.insa.graphs.model;

public class Label implements Comparable<Label> {

    private Node node;
    private boolean mark;
    private double cost;
    private int fatherId;

    /* Constructeur */
    public Label(Node node, boolean mark, double cost, int fatherId) {
        this.node = node;
        this.mark = mark;
        this.cost = cost;
        this.fatherId = fatherId;
    }

    public Node getNode() {
        return this.node;
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

    public double getTotalCost(){
        return cost;
    }

    public double getEstimateCost(){
        return 0;
    }

    @Override
    public int compareTo(Label arg0) {
        return Double.compare(this.cost, arg0.cost);
    }
}