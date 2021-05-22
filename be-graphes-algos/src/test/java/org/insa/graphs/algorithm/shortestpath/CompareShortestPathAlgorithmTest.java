package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.BinaryPathReader;
import org.insa.graphs.model.io.GraphReader;
import org.insa.graphs.model.io.PathReader;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;

import static org.insa.graphs.algorithm.AbstractSolution.Status;
import static org.insa.graphs.algorithm.ArcInspectorFactory.FilterType;
import static org.insa.graphs.algorithm.ArcInspectorFactory.getAllFilters;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public abstract class CompareShortestPathAlgorithmTest {

        public abstract ShortestPathAlgorithm launchTestShortestPathAlgorithm(ShortestPathData data);

        protected static class TestParameters {
                public final ShortestPathData data;
                public final ShortestPathSolution solution;
                private final String description;

                /* Method for test different parameters */
                public TestParameters(ShortestPathData data, ShortestPathSolution solution, String description) {
                        this.data = data;
                        this.solution = solution;
                        this.description = description;
                }

                @Override
                public String toString() {
                        return description;
                }
        }

        @Parameters(name = "{0}")
        public static Collection<Object> data() throws Exception {
                Collection<Object> objects = new ArrayList<>();

                /**
                 * TEST MAP : Haute-Garonne
                 */

                /* Insert the Haute-Garonne test map */
                final String hauteGaronneMapPathName = "C:/Users/romro/Desktop/git/Graphes/Maps/haute-garonne.mapgr";
                final GraphReader graphReader = new BinaryGraphReader(new DataInputStream(
                                new BufferedInputStream(new FileInputStream(hauteGaronneMapPathName))));
                final Graph graph = graphReader.read(); /* Read the map */

                /**
                 * TEST PATHS : Fastest/Shortest/Only cars/Destination = Origin/Infeasible paths
                 */

                /* Haute-Garonne fastest path test solution */
                final String timeTestPathName = "C:/Users/romro/Desktop/git/Graphes/Maps/path/path_fr31_time_test.path";
                ShortestPathSolution timeTestSolution = createRegularTestSolution(graph, timeTestPathName,
                                FilterType.ALL_ROADS_AND_TIME);

                /* Haute-Garonne shortest path test solution */
                final String lengthTestPathName = "C:/Users/romro/Desktop/git/Graphes/Maps/path/path_fr31_length_test.path";
                ShortestPathSolution lengthTestSolution = createRegularTestSolution(graph, lengthTestPathName,
                                FilterType.ALL_ROADS_AND_LENGTH);

                /* Haute-Garonne only roads for cars test path solution */
                final String roadsForCarsPathName = "C:/Users/romro/Desktop/git/Graphes/Maps/path/path_fr31_roads_for_cars_test.path";
                ShortestPathSolution roadsForCarsSolution = createRegularTestSolution(graph, roadsForCarsPathName,
                                FilterType.ONLY_CARS_AND_LENGTH);

                /* Haute-Garonne origin equals destination */
                ShortestPathData originEqualsDestinationTestData = new ShortestPathData(graph, graph.get(15),
                                graph.get(15), getAllFilters().get(FilterType.ALL_ROADS_AND_LENGTH.getValue()));
                final Path originEqualsDestinationPath = new Path(originEqualsDestinationTestData.getGraph(),
                                originEqualsDestinationTestData.getOrigin());
                ShortestPathSolution originEqualsDestinationSolution = new ShortestPathSolution(
                                originEqualsDestinationTestData, Status.OPTIMAL, originEqualsDestinationPath);

                /* Haute-Garonne unreachable destination */
                ShortestPathData unreachableDestinationData = new ShortestPathData(graph, graph.get(120349),
                                graph.get(120351), getAllFilters().get(FilterType.ALL_ROADS_AND_LENGTH.getValue()));
                ShortestPathSolution unreachableDestinationPath = new ShortestPathSolution(unreachableDestinationData,
                                Status.INFEASIBLE);

                /**
                 * TEST CALLS
                 */

                objects.add(new TestParameters(timeTestSolution.getInputData(), timeTestSolution, "Fastest test"));
                objects.add(new TestParameters(lengthTestSolution.getInputData(), lengthTestSolution, "Shortest test"));
                objects.add(new TestParameters(roadsForCarsSolution.getInputData(), roadsForCarsSolution,
                                "Cars only test"));
                objects.add(new TestParameters(originEqualsDestinationTestData, originEqualsDestinationSolution,
                                "Destination equal origin test"));
                objects.add(new TestParameters(unreachableDestinationData, unreachableDestinationPath,
                                "Unfeasible test"));

                return objects;
        }

        private static ShortestPathSolution createRegularTestSolution(Graph graph, String solutionPathFileName,
                        FilterType filter) throws Exception {
                final PathReader roadsForCarsPathReader = new BinaryPathReader(new DataInputStream(
                                new BufferedInputStream(new FileInputStream(solutionPathFileName))));
                final Path roadsForCarsSolutionPath = roadsForCarsPathReader.readPath(graph);

                ShortestPathData roadsForCarsSolutionData = new ShortestPathData(graph,
                                roadsForCarsSolutionPath.getOrigin(), roadsForCarsSolutionPath.getDestination(),
                                getAllFilters().get(filter.getValue()));

                return new ShortestPathSolution(roadsForCarsSolutionData, Status.OPTIMAL, roadsForCarsSolutionPath);
        }

        @Parameter
        public TestParameters parameters;

        private ShortestPathSolution computedSolution;

        /**
         * Run the algorithm to test (DijkstraAlgorithm, AStarAlgorithm...)
         */
        @Before
        public void init() {
                ShortestPathAlgorithm algorithm = launchTestShortestPathAlgorithm(parameters.data);
                computedSolution = algorithm.doRun();
        }

        /**
         * Test
         */
        @Test
        public void testPathValid() {
                Assume.assumeFalse(parameters.solution.getStatus() == Status.INFEASIBLE);
                assertTrue(computedSolution.getPath().isValid());
        }

        @Test
        public void testPathCostCorrect() {
                Assume.assumeFalse(parameters.solution.getStatus() == Status.INFEASIBLE);
                assertEquals(parameters.solution.getPath().getLength(), computedSolution.getPath().getLength(), 1e-3);
                assertEquals(parameters.solution.getPath().getMinimumTravelTime(),
                                computedSolution.getPath().getMinimumTravelTime(), 1e-3);
        }

        @Test
        public void testSolutionCorrect() {
                if (parameters.solution.getStatus() != Status.INFEASIBLE) {
                        assertEquals(parameters.solution.getPath(), computedSolution.getPath());
                }
                assertEquals(parameters.solution.getStatus(), computedSolution.getStatus());
        }

}