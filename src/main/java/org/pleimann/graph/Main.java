package org.pleimann.graph;

import org.jgrapht.DirectedGraph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.traverse.RandomWalkIterator;
import org.pleimann.graph.model.SeaPort;
import org.pleimann.graph.model.ShippingLeg;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String... args) throws URISyntaxException {
        Path dataPath = Paths.get(Main.class.getResource("/seaports.json").toURI());

        DirectedGraph<SeaPort, ShippingLeg> graph = SeaPortGraphFactory.generateGraph(dataPath);

        long startTime;

        LinkedList<SeaPort> ps = SeaPortGraphFactory.generateRandomWalk(8, graph);
        printPath(ps);

        SeaPort poe = ps.getFirst(), pod = ps.getLast();

        startTime = System.nanoTime();
        KShortestPaths<SeaPort, ShippingLeg> paths = new KShortestPaths(graph, 5);
        List<GraphPath<SeaPort, ShippingLeg>> shortestPaths = paths.getPaths(poe, pod);
        System.out.println("Shortest Paths: " + TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS) + "ms");
        shortestPaths.forEach(path -> printPath(path.getVertexList()));

//        GraphDisplay<SeaPort, ShippingLeg> graphDisplay = new GraphDisplay<>(graph);
//        graphDisplay.setSize(800, 600);
//        graphDisplay.pack();
//
//        graphDisplay.setVisible(true);

        System.exit(0);
    }

    private static void printPath(List<SeaPort> path){
        final SeaPort pod = path.get(path.size() - 1);
        path.forEach((port) -> System.out.print(port == pod ? "" : port.getId() + " -> "));
        System.out.println(pod.getId() + "\n");
    }
}
