package org.insa.graphs.algorithm.shortestpath;

public class DijkstraAlgorithmTest extends AllShortestPathAlgorithmTest {
    @Override
    public ShortestPathAlgorithm launchShortestPathAlgorithm(ShortestPathData data) {
        return new DijkstraAlgorithm(data);
    }
}