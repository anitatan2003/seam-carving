package seamcarving;

import graphs.Edge;
import graphs.Graph;
import graphs.shortestpaths.DijkstraShortestPathFinder;
import graphs.shortestpaths.ShortestPath;
import graphs.shortestpaths.ShortestPathFinder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class DijkstraSeamFinder implements SeamFinder {
    private final ShortestPathFinder<Graph<Pixel, Edge<Pixel>>, Pixel, Edge<Pixel>> pathFinder;

    public DijkstraSeamFinder() {
        this.pathFinder = createPathFinder();
    }

    protected <G extends Graph<V, Edge<V>>, V> ShortestPathFinder<G, V, Edge<V>> createPathFinder() {
        /*
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
        */
        return new DijkstraShortestPathFinder<>();
    }

    @Override
    public List<Integer> findHorizontalSeam(double[][] energies) {
        energies = transposeArray(energies);
        PixelGraph graph= new PixelGraph(energies);
        ShortestPath<Pixel, Edge<Pixel>> pathEdges = pathFinder.findShortestPath(graph,
            graph.dummyFront, graph.dummyEnd);

        List<Pixel> edges = pathEdges.vertices();
        List<Integer> seam = new ArrayList<>();
        for (int i = edges.size() - 1; i > 0; i--) {
            seam.add(edges.get(i).getCol());
        }
        seam.remove(0);
        return seam;
    }

    private static double[][] transposeArray(double[][] array) {
        int rows = array.length;
        int columns = array[0].length;

        // Create a new array with dimensions reversed
        double[][] rotatedArray = new double[columns][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                rotatedArray[j][rows - i - 1] = array[i][j];
            }
        }
        return rotatedArray;
    }

    @Override
    public List<Integer> findVerticalSeam(double[][] energies) {
        PixelGraph graph = new PixelGraph(energies);

        List<Integer> seam = new ArrayList<>();

        ShortestPath<Pixel, Edge<Pixel>> shortestPath = pathFinder.findShortestPath(graph,
            graph.dummyFront, graph.dummyEnd);

        List<Pixel> pathEdges = shortestPath.vertices();
        for (Pixel p : pathEdges) {
            seam.add(p.getCol());
        }

        seam.remove(0);
        seam.remove(seam.size() - 1);
        return seam;
    }

    public class PixelGraph implements Graph<Pixel, Edge<Pixel>> {
        Map<Pixel, List<Edge<Pixel>>> adjList;
        private double[][] energies;
        public Pixel dummyFront;
        public Pixel dummyEnd;

        public PixelGraph(double[][] energies) {
            this.dummyFront = new Pixel(-1, -1);
            this.dummyEnd = new Pixel(-2, -2);
            this.adjList = new HashMap<>();
            this.energies = energies;
        }

        @Override
        public Collection<Edge<Pixel>> outgoingEdgesFrom(Pixel vertex) {
            Set<Edge<Pixel>> outgoingEdges = new HashSet<>();
            int row = vertex.getRow();
            int col = vertex.getCol();
            if (vertex.equals(dummyFront)) { //Objects.equals(vertex, dummyFront)
                for (int i = 0; i < energies.length; i++) {
                    Pixel destVertex1 = new Pixel(0, i);
                    outgoingEdges.add(new Edge<>(vertex, destVertex1,
                        energies[destVertex1.getCol()][destVertex1.getRow()]));
                }
            } else if (vertex.getRow() == energies[0].length - 1) {
                outgoingEdges.add(new Edge<>(vertex, dummyEnd, 0));
            } else {
                if (col == 0) {
                    Pixel destVertex1 = new Pixel(row + 1, col);
                    //double weight1 = energies[destVertex1.getCol()][destVertex1.getRow()];
                    outgoingEdges.add(new Edge<>(vertex, destVertex1,
                        energies[destVertex1.getCol()][destVertex1.getRow()]));

                    Pixel destVertex2 = new Pixel(row + 1, col + 1);
                    //double weight2 = energies[destVertex2.getCol()][destVertex2.getRow()];
                    outgoingEdges.add(new Edge<>(vertex, destVertex2,
                        energies[destVertex2.getCol()][destVertex2.getRow()]));
                } else if (col == energies.length - 1) {
                    Pixel destVertex1 = new Pixel(row + 1, col - 1);
                    //double weight1 = energies[destVertex1.getCol()][destVertex1.getRow()];
                    outgoingEdges.add(new Edge<>(vertex, destVertex1,
                        energies[destVertex1.getCol()][destVertex1.getRow()]));

                    Pixel destVertex2 = new Pixel(row + 1, col);
                    //double weight2 = energies[destVertex2.getCol()][destVertex2.getRow()];
                    outgoingEdges.add(new Edge<>(vertex, destVertex2,
                        energies[destVertex2.getCol()][destVertex2.getRow()]));
                } else {
                    Pixel destVertex1 = new Pixel(row + 1, col - 1);
                    //double weight1 = energies[destVertex1.getCol()][destVertex1.getRow()];
                    outgoingEdges.add(new Edge<>(vertex, destVertex1,
                        energies[destVertex1.getCol()][destVertex1.getRow()]));

                    Pixel destVertex2 = new Pixel(row + 1, col);
                    //double weight2 = energies[destVertex2.getCol()][destVertex2.getRow()];
                    outgoingEdges.add(new Edge<>(vertex, destVertex2,
                        energies[destVertex2.getCol()][destVertex2.getRow()]));

                    Pixel destVertex3 = new Pixel(row + 1, col + 1);
                    //double weight3 = energies[destVertex3.getCol()][destVertex3.getRow()];
                    outgoingEdges.add(new Edge<>(vertex, destVertex3,
                        energies[destVertex3.getCol()][destVertex3.getRow()]));
                }
            }
            return outgoingEdges;
        }
    }

    public class Pixel {
        private int row;
        private int col;

        public Pixel(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Pixel current = (Pixel) o;
            return row == current.row && col == current.col;
        }
        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }
    }
}
