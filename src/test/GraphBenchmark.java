package com.routefinder.routefinder;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@Measurement(iterations=10)
@Warmup(iterations=5)
@Fork(value=1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
public class GraphBenchmark {

    private Graph<String> graph;
    private Vertex<String> source;
    private Vertex<String> destination;

    @Setup(Level.Trial)
    public void setup() {
        graph = new Graph<>();
        source = new Vertex<>("Source");
        destination = new Vertex<>("Destination");
        graph.addVertex(source);
        graph.addVertex(destination);
        for (int i = 0; i < 100; i++) {
            Vertex<String> middle = new Vertex<>("Vertex" + i);
            graph.addVertex(middle);
            graph.addEdge(source, middle, Math.random() * 10, "Road" + i, Math.random() * 10);
            graph.addEdge(middle, destination, Math.random() * 10, "RoadD" + i, Math.random() * 10);
        }
    }

    @Benchmark
    public void testDijkstra() {
        graph.dijkstra(source, destination);
    }

    @Benchmark
    public void testDijkstraCultural() {
        graph.dijkstraCultural(source, destination);
    }

    @Benchmark
    public void testAddVertex() {
        Vertex<String> newVertex = new Vertex<>("New");
        graph.addVertex(newVertex);
    }

    @Benchmark
    public void testAddEdge() {
        graph.addEdge(source, destination, 1.0, "NewStreet", 1.0);
    }
}