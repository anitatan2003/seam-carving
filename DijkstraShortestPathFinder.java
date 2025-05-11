package graphs.shortestpaths;

import priorityqueues.ExtrinsicMinPQ;
import priorityqueues.NaiveMinPQ;
import graphs.BaseEdge;
import graphs.Graph;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Computes shortest paths using Dijkstra's algorithm.
 * @see SPTShortestPathFinder for more documentation.
 */
public class DijkstraShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    extends SPTShortestPathFinder<G, V, E> {

    protected <T> ExtrinsicMinPQ<T> createMinPQ() {
        return new NaiveMinPQ<>();
        /*
        If you have confidence in your heap implementation, you can disable the line above
        and enable the one below.
         */
        // return new ArrayHeapMinPQ<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    protected Map<V, E> constructShortestPathsTree(G graph, V start, V end) {
        ExtrinsicMinPQ<V> pq = createMinPQ();
        Map<V, E> edgeTo = new HashMap<>();
        Map<V, Double> disTo = new HashMap<>();
        disTo.put(start, 0.0);
        pq.add(start, 0.0);

        while (!pq.isEmpty()) {
            V u = pq.removeMin();
            if (Objects.equals(u, end)) {
               return edgeTo;
            }

            for (E edge : graph.outgoingEdgesFrom(u)) {
                V v = edge.to();
                double weight = edge.weight();
                double oldDist = disTo.getOrDefault(v, Double.POSITIVE_INFINITY);
                double newDist = disTo.get(u) + weight;

                if (newDist < oldDist) {
                    disTo.put(v, newDist);
                    edgeTo.put(v, edge);

                    if (pq.contains(v)) {
                        pq.changePriority(v, newDist);
                    } else {
                        pq.add(v, newDist);
                    }
                }
            }
        }
        System.out.println("test: " + edgeTo.containsKey("s"));
        return edgeTo;
    }


    @Override
    protected ShortestPath<V, E> extractShortestPath(Map<V, E> spt, V start, V end) {

        if (Objects.equals(start, end)) {
            return new ShortestPath.SingleVertex<>(start);
        }

        if (!spt.containsKey(end)) {
            return new ShortestPath.Failure<>();
        }
        if (spt.get(end) == null) {
            return new ShortestPath.Failure<>();
        }


        List<E> shortestpathEdges = new ArrayList<>();
        V currentVertex = end;
        while (spt.get(currentVertex) != null) {
            E edge = spt.get(currentVertex);
            shortestpathEdges.add(edge);
            currentVertex = edge.from();
        }

        Collections.reverse(shortestpathEdges);
        return new ShortestPath.Success<>(shortestpathEdges);
    }
}
