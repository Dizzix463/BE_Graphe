package org.insa.graphs.algorithm.shortestpath;

public class AStarAlgorithmTest extends AllShortestPathAlgorithmTest {
    @Override
    public ShortestPathAlgorithm launchShortestPathAlgorithm(ShortestPathData data) {
        return new AStarAlgorithm(data);
    }
}