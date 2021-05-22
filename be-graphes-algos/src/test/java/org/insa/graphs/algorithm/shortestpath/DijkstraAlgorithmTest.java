package org.insa.graphs.algorithm.shortestpath;

public class DijkstraAlgorithmTest extends CompareShortestPathAlgorithmTest {
    @Override
    public ShortestPathAlgorithm launchTestShortestPathAlgorithm(ShortestPathData data) {
        return new DijkstraAlgorithm(data);
    }
}