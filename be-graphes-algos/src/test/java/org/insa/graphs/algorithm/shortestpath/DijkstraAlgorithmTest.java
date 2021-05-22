package org.insa.graphs.algorithm.shortestpath;

public class DijkstraAlgorithmTest extends ShortestPathAlgorithmTest {
    @Override
    public ShortestPathAlgorithm launchTestShortestPathAlgorithm(ShortestPathData data) {
        return new DijkstraAlgorithm(data);
    }
}