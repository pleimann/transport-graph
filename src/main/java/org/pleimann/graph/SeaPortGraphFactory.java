package org.pleimann.graph;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;
import org.jgrapht.VertexFactory;
import org.jgrapht.generate.WeightedGraphGenerator;
import org.jgrapht.graph.ClassBasedVertexFactory;
import org.jgrapht.graph.ListenableDirectedWeightedGraph;
import org.jgrapht.graph.builder.DirectedWeightedGraphBuilder;
import org.jgrapht.traverse.RandomWalkIterator;
import org.pleimann.graph.model.SeaPort;
import org.pleimann.graph.model.ShippingLeg;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SeaPortGraphFactory {
    public static final DirectedGraph<SeaPort, ShippingLeg> generateGraph(Path dataPath){
        long startTime = System.nanoTime();

        DirectedWeightedGraphBuilder<SeaPort, ShippingLeg, ListenableDirectedWeightedGraph<SeaPort, ShippingLeg>> builder =
                new DirectedWeightedGraphBuilder<>(new ListenableDirectedWeightedGraph<>(ShippingLeg.class));

        try (BufferedReader reader = Files.newBufferedReader(dataPath, StandardCharsets.UTF_8)) {
            ObjectMapper objectMapper = new ObjectMapper();

            Random rand = new Random(System.nanoTime());

            SeaPort[] ports = objectMapper.readValue(reader, SeaPort[].class);
            for (SeaPort port : ports) {
                builder.addVertex(port);
            }

            int edgeCount;
            SeaPort targetPort;
            for (SeaPort port : ports) {
                edgeCount = rand.nextInt() % 10;
                for (int i = 0; i < edgeCount; i++) {
                    do {
                        targetPort = ports[Math.round(Math.max(0, rand.nextFloat() * ports.length - 1))];

                    } while (targetPort.equals(port));

                    builder.addEdge(port, targetPort, new ShippingLeg(BigDecimal.valueOf(rand.nextInt(), 2)));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Generate Graph: " + TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS) + "ms\n");

        return builder.buildUnmodifiable();
    }

    public static LinkedList<SeaPort> generateRandomWalk(int length, DirectedGraph<SeaPort, ShippingLeg> graph) {
        SeaPort poe;

        Random rand = new Random(System.nanoTime());

        long startTime = System.nanoTime();

        SeaPort[] ports = graph.vertexSet().toArray(new SeaPort[0]);
        do {
            poe = ports[Math.round(Math.max(0, rand.nextFloat() * ports.length - 1))];

        } while (graph.outDegreeOf(poe) == 0);

        LinkedList<SeaPort> path;
        do {
            path = new LinkedList<>();
            path.add(poe);
            RandomWalkIterator<SeaPort, ShippingLeg> walk = new RandomWalkIterator<>(graph, poe, true, 30);

            while (walk.hasNext()) {
                path.add(walk.next());
            }

        } while (path.size() <= length);

        System.out.println("Random Walk: " + TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS) + "ms");

        return path;
    }
}
