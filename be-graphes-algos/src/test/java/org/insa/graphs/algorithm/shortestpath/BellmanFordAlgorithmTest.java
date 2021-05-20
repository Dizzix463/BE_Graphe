package org.insa.graphs.algorithm.shortestpath;

public class BellmanFordAlgorithmTest extends AllShortestPathAlgorithmTest {
    @Override
    public ShortestPathAlgorithm launchShortestPathAlgorithm(ShortestPathData data) {
        return new BellmanFordAlgorithm(data);
    }
}