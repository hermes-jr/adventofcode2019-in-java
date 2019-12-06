package level_06;


import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.DepthFirstIterator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Objects;

public class Level06 {

    SimpleGraph<String, DefaultEdge> readResources(String filename) {
        SimpleGraph<String, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);

        try (BufferedReader br
                     = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("level_06/" + filename))))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] split = line.split("\\)");
                g.addVertex(split[0]);
                g.addVertex(split[1]);
                g.addEdge(split[0], split[1]);
            }
            return g;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't read resources", e);
        }
    }

    int p1(SimpleGraph<String, DefaultEdge> g) {
        Iterator<String> iterator = new DepthFirstIterator<>(g, "COM");

        DijkstraShortestPath<String, DefaultEdge> dijkstraAlg =
                new DijkstraShortestPath<>(g);
        ShortestPathAlgorithm.SingleSourcePaths<String, DefaultEdge> iPaths = dijkstraAlg.getPaths("COM");
        int indirect = 0;
        while (iterator.hasNext()) {
            String node = iterator.next();
            GraphPath<String, DefaultEdge> pathToRoot = iPaths.getPath(node);
            indirect += Math.max(0, pathToRoot.getLength() - 1);
        }

        int direct = g.edgeSet().size();
        return direct + indirect;
    }

    int p2(SimpleGraph<String, DefaultEdge> g) {
        DijkstraShortestPath<String, DefaultEdge> dijkstraAlg =
                new DijkstraShortestPath<>(g);
        ShortestPathAlgorithm.SingleSourcePaths<String, DefaultEdge> iPaths = dijkstraAlg.getPaths("SAN");
        GraphPath<String, DefaultEdge> toSanta = iPaths.getPath("YOU");
        return Math.max(0, toSanta.getLength() - 2);
    }

    public static void main(String[] args) {
        Level06 l = new Level06();
        SimpleGraph<String, DefaultEdge> g = l.readResources("input");


        System.out.println("Part1: " + l.p1(g));
        System.out.println("Part2: " + l.p2(g));
    }

}
