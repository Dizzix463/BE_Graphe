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

    @Override
    protected ShortestPathSolution doRun() {

        final ShortestPathData data = getInputData();

        Graph graph = data.getGraph();
        Label[] labels = new Label[graph.size()];

        /* Associate a label for a node */
        for (int i = 0; i < graph.size(); i++) {
            labels[i] = new Label(i, false, Double.POSITIVE_INFINITY, -1);
        }

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
            labels[currentNodeLabel.getNodeId()].setMark(true);

            for (Arc successor : graph.get(currentNodeLabel.getNodeId()).getSuccessors()) {
                if (!data.isAllowed(successor))
                    continue;

                int nextNodeId = successor.getDestination().getId();
                if (!labels[nextNodeId].isMark()) {
                    double w = data.getCost(successor);
                    double oldDistance = labels[nextNodeId].getCost();
                    double newDistance = labels[currentNodeLabel.getNodeId()].getCost() + w;

                    if (Double.isInfinite(oldDistance) && Double.isFinite(newDistance)) {
                        notifyNodeReached(successor.getDestination());
                    }

                    /* Check if new distances would be better, if so update */
                    if (newDistance < oldDistance) {
                        labels[nextNodeId].setCost(newDistance);
                        labels[nextNodeId].setFatherId(currentNodeLabel.getNodeId());

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
