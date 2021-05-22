package org.insa.graphs.algorithm.shortestpath;

public class AStarAlgorithmTest extends CompareShortestPathAlgorithmTest {
    @Override
    public ShortestPathAlgorithm launchTestShortestPathAlgorithm(ShortestPathData data) {
        return new AStarAlgorithm(data);
    }
}