package com.routefinder.routefinder;

import java.util.List;
import java.util.ArrayList;

public class Vertex<T> {
    private T data;
    private List<Edge<T>> edges;

    public Vertex(T data) {
        this.data = data;
        this.edges = new ArrayList<>();
    }

    public void addEdge(Vertex<T> destination, double weight, String streetName, double culturalWeight) {
        edges.add(new Edge<>(destination, weight, streetName, culturalWeight));
        destination.edges.add(new Edge<>(this, weight, streetName,culturalWeight));
    }

    public List<Edge<T>> getEdges() {
        return edges;
    }

    public T getData() {
        return data;
    }
}
