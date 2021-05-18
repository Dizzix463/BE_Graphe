package org.insa.graphs.model;

public class LabelStar extends Label {

    private double estimate_cost;

    public LabelStar(Node node, boolean mark, double cost, int fatherId, Node destNode) {
        super(node, mark, cost, fatherId);
        this.estimate_cost = node.getPoint().distanceTo(destNode.getPoint());
    }

    @Override
    public double getEstimateCost() {
        return estimate_cost;
    }

    @Override
    public double getTotalCost() {
        return this.getCost() + this.estimate_cost;
    }
}
