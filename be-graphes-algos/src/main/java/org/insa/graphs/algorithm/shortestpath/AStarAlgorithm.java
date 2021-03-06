package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.*;
import static org.insa.graphs.algorithm.AbstractInputData.*;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }

    /**
     * Rewriting existing method in DijkstraAlgorithm
     */
    @Override
    public Label[] initTabLabel(int nbNode, ShortestPathData data) {

        /* Init LabelStar label list */
        LabelStar[] labels = new LabelStar[nbNode];

        /* Parameters */
        double cost = Double.POSITIVE_INFINITY;
        Node destNode = data.getDestination();
        boolean LengthMode = data.getMode() == Mode.LENGTH;

        /* Add LabelStar labels to the list */
        for (int i = 0; i < nbNode; i++) {
            labels[i] = new LabelStar(data.getGraph().get(i), false, cost, -1, destNode, LengthMode);
        }
        return labels;
    }

}
