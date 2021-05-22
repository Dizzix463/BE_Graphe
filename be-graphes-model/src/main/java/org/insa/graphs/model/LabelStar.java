package org.insa.graphs.model;

/**
 * LabelStar inherits from the class Label
 */
public class LabelStar extends Label {

    private double estimate_cost;

    public LabelStar(Node node, boolean mark, double cost, int fatherId, Node destNode, boolean Mode) {
        super(node, mark, cost, fatherId);

        if (Mode) {
            this.estimate_cost = node.getPoint().distanceTo(destNode.getPoint());
        } else {
            this.estimate_cost = node.getPoint().distanceTo(destNode.getPoint()) / 25d;
        }
    }

    @Override
    public double getEstimateCost() {
        return estimate_cost;
    }

    @Override
    public double getTotalCost() {
        return getCost() + estimate_cost;
    }
}
