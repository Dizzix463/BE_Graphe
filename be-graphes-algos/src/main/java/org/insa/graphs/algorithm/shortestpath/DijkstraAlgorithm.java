package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractInputData;
import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.algorithm.utils.ElementNotFoundException;
import org.insa.graphs.algorithm.utils.EmptyPriorityQueueException;
import org.insa.graphs.model.*;

import java.util.ArrayList;
import java.util.Collections;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    Label[] initTabLabel(int nbNode, ShortestPathData data) {
        Label[] labelTab = new Label[nbNode];
        Graph graph = data.getGraph();
        for (int i = 0; i < nbNode; i++) {
            labelTab[i] = new Label(graph.get(i), false, Double.POSITIVE_INFINITY, -1);
        }
        return labelTab;
    }

    @Override
    protected ShortestPathSolution doRun() {

        final ShortestPathData data = getInputData();
        Graph graph = data.getGraph();

        /* Init the table of labels */
        Label[] labels = initTabLabel(graph.size(), data);

        /* Initialize the origin */
        labels[data.getOrigin().getId()].setCost(0);

        /* Initialise binaryHeap for min of each event */
        BinaryHeap<Label> binaryHeap = new BinaryHeap<Label>();

        /* Add the origin into binaryHeap */
        binaryHeap.insert(labels[data.getOrigin().getId()]);

        /* Notify to the Observer */
        notifyOriginProcessed(data.getOrigin());

        /* Main algorithme */
        while (!labels[data.getDestination().getId()].isMark()) {
            Label currentNodeLabel;
            try {
                currentNodeLabel = binaryHeap.findMin();
            } catch (EmptyPriorityQueueException e) {
                /**
                 * Try if a new node is marked after the last, although it is the only one
                 * visited but not marked So We reach and mark all nodes
                 */
                break;
            }

            /* Mark the node */
            try {
                binaryHeap.remove(currentNodeLabel);
            } catch (ElementNotFoundException ignored) {
            }
            labels[currentNodeLabel.getNode().getId()].setMark(true);

            for (Arc successor : graph.get(currentNodeLabel.getNode().getId()).getSuccessors()) {
                if (!data.isAllowed(successor))
                    continue;

                int currentNodeId = successor.getOrigin().getId();
                int nextNodeId = successor.getDestination().getId();
                if (!labels[nextNodeId].isMark()) {
                    double oldCost = labels[nextNodeId].getTotalCost();
                    double w = data.getCost(successor) + labels[nextNodeId].getEstimateCost();
                    double newCost = labels[currentNodeId].getCost() + w;

                    if (Double.isInfinite(oldCost) && Double.isFinite(newCost)) {
                        notifyNodeReached(successor.getDestination());
                    }

                    /* Check if new distances would be better, if so update */
                    if (newCost < oldCost) {
                        labels[nextNodeId].setCost(labels[currentNodeId].getCost() + data.getCost(successor));
                        labels[nextNodeId].setFatherId(currentNodeLabel.getNode().getId());

                        try {
                            /* Update the node in the binaryHeap */
                            binaryHeap.remove(labels[nextNodeId]);
                            binaryHeap.insert(labels[nextNodeId]);
                        } catch (ElementNotFoundException e) {
                            /* If the node is not in the binaryHeap, insert it */
                            binaryHeap.insert(labels[nextNodeId]);
                        }
                    }
                }
            }
        }

        ShortestPathSolution solution;

        if (!labels[data.getDestination().getId()].isMark()) {
            solution = new ShortestPathSolution(data, AbstractSolution.Status.INFEASIBLE);
        } else {
            /* The destination has been found, notify the observers. */
            notifyDestinationReached(data.getDestination());

            ArrayList<Node> pathNodes = new ArrayList<>();
            pathNodes.add(data.getDestination());
            Node node = data.getDestination();
            while (!node.equals(data.getOrigin())) {
                Node fatherNode = graph.getNodes().get(labels[node.getId()].getFatherId());
                pathNodes.add(fatherNode);
                node = fatherNode;
            }
            Collections.reverse(pathNodes);

            /* Create the final solution. */
            Path solutionPath;
            if (data.getMode().equals(AbstractInputData.Mode.LENGTH)) {
                solutionPath = Path.createShortestPathFromNodes(graph, pathNodes);
            } else {
                solutionPath = Path.createFastestPathFromNodes(graph, pathNodes);
            }

            solution = new ShortestPathSolution(data, AbstractSolution.Status.OPTIMAL, solutionPath);
        }

        return solution;
    }

}
