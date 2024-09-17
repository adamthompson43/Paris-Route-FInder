
package com.routefinder.routefinder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {
    private Graph<String> graph;

    @BeforeEach
    void setUp() {
        graph = new Graph<>();
        Vertex<String> a = new Vertex<>("A");
        Vertex<String> b = new Vertex<>("B");
        Vertex<String> c = new Vertex<>("C");

        graph.addVertex(a);
        graph.addVertex(b);
        graph.addVertex(c);

        graph.addEdge(a, b, 1.0, "Street1", 1.0);
        graph.addEdge(b, c, 1.0, "Street2", 1.0);
        graph.addEdge(c, a, 1.5, "Street3", 1.5);
    }

    @Test
    void testAddVertex() {
        Vertex<String> d = new Vertex<>("D");
        graph.addVertex(d);
        assertNotNull(graph.findVertexByName("D"));
    }

    @Test
    void testFindVertexByName() {
        Vertex<String> result = graph.findVertexByName("A");
        assertNotNull(result);
        assertEquals("A", result.getData());
    }

    @Test
    void testAddEdgeAndDijkstra() {
        Vertex<String> source = graph.findVertexByName("A");
        Vertex<String> destination = graph.findVertexByName("C");
        List<Vertex<String>> path = graph.dijkstra(source, destination);

        assertNotNull(path);
        assertFalse(path.isEmpty());
        assertEquals("A", path.get(0).getData());
        assertEquals("C", path.get(path.size() - 1).getData());
    }

    @Test
    void testDijkstraCultural() {
        Vertex<String> source = graph.findVertexByName("A");
        Vertex<String> destination = graph.findVertexByName("C");
        List<Vertex<String>> path = graph.dijkstraCultural(source, destination);

        assertNotNull(path);
        assertFalse(path.isEmpty());
        assertEquals("A", path.get(0).getData());
        assertEquals("C", path.get(path.size() - 1).getData());
    }

    @Test
    void testDFSPaths() {
        Vertex<String> source = graph.findVertexByName("A");
        Vertex<String> destination = graph.findVertexByName("C");
        List<List<Vertex<String>>> paths = graph.dfsPaths(source, destination, 10);

        assertFalse(paths.isEmpty());
        assertTrue(paths.stream().anyMatch(path -> path.contains(source) && path.contains(destination)));
    }
}
