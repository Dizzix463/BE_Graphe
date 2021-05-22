package org.insa.graphs.algorithm.shortestpath;

public class AStarAlgorithmTest extends ShortestPathAlgorithmTest {
    @Override
    public ShortestPathAlgorithm launchTestShortestPathAlgorithm(ShortestPathData data) {
        return new AStarAlgorithm(data);
    }
}