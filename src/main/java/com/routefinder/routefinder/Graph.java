package com.routefinder.routefinder;


import java.util.*;

public class Graph<T> {
    private List<Vertex<T>> vertices;

    public Graph() {
        this.vertices = new ArrayList<>();
    }

    public void addVertex(Vertex<T> vertex) {
        vertices.add(vertex);
    }

    public Vertex<T> findVertexByName(String name) {
        for (Vertex<T> vertex : vertices) {
            if (vertex.getData().toString().contains(name)) {
                return vertex;
            }
        }
        return null;
    }

    public void addEdge(Vertex<T> source, Vertex<T> destination, double weight, String streetName, double culturalWeight) {
        source.addEdge(destination, weight, streetName, culturalWeight);
    }

    public List<Vertex<T>> dijkstra(Vertex<T> source, Vertex<T> destination) {
        Map<Vertex<T>, Double> shortestDistances = new HashMap<>(); //store the shortest distance from source vertex
        Map<Vertex<T>, Vertex<T>> predecessors = new HashMap<>(); // store previous vertex in the shortest path
        PriorityQueue<Vertex<T>> queue = new PriorityQueue<>(Comparator.comparing(shortestDistances::get)); //priority queue uses vertex with shortest distance

        shortestDistances.put(source, 0.0);
        queue.add(source);

        while (!queue.isEmpty()) {
            Vertex<T> current = queue.poll();

            // stop if destinaton reached
            if (current.equals(destination)) {
                break;
            }

            for (Edge<T> edge : current.getEdges()) {
                Vertex<T> neighbor = edge.getDestination();
                double newDistance = shortestDistances.get(current) + edge.getWeight(); //find distance to neighbour vertex
                if (newDistance < shortestDistances.getOrDefault(neighbor, Double.MAX_VALUE)) { //if shorter than previous distance
                    shortestDistances.put(neighbor, newDistance);
                    predecessors.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        return reconstructPath(predecessors, source, destination);
    }

    public List<Vertex<T>> dijkstraCultural(Vertex<T> source, Vertex<T> destination) {
        Map<Vertex<T>, Double> shortestDistances = new HashMap<>();//store the shortest distance from source vertex
        Map<Vertex<T>, Vertex<T>> predecessors = new HashMap<>();// store previous vertex in the shortest path
        PriorityQueue<Vertex<T>> queue = new PriorityQueue<>(Comparator.comparing(shortestDistances::get));//priority queue uses vertex with shortest distance

        shortestDistances.put(source, 0.0);
        queue.add(source);

        while (!queue.isEmpty()) {
            Vertex<T> current = queue.poll();

            // stop if destinaton reached
            if (current.equals(destination)) {
                break;
            }

            for (Edge<T> edge : current.getEdges()) {
                Vertex<T> neighbor = edge.getDestination();
                double newDistance = shortestDistances.get(current) + edge.getCulturalWeight();//find distance to neighbour vertex
                if (newDistance < shortestDistances.getOrDefault(neighbor, Double.MAX_VALUE)) {//if shorter than previous distance
                    shortestDistances.put(neighbor, newDistance);
                    predecessors.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        return reconstructPath(predecessors, source, destination);
    }
    private List<Vertex<T>> reconstructPath(Map<Vertex<T>, Vertex<T>> predecessors, Vertex<T> source, Vertex<T> destination) {
        LinkedList<Vertex<T>> path = new LinkedList<>();
        Vertex<T> step = destination;

        // Check if path exists
        if (predecessors.get(step) == null) {
            return path;
        }

        path.add(step);
        while (predecessors.get(step) != null) {
            step = predecessors.get(step);
            path.add(step);
        }

        Collections.reverse(path);
        return path;
    }


    // intitiates DFS
    public List<List<Vertex<T>>> dfsPaths(Vertex<T> source, Vertex<T> destination, int maxRoutes) {
        List<List<Vertex<T>>> allPaths = new ArrayList<>();
        List<Vertex<T>> currentPath = new ArrayList<>();
        Set<Vertex<T>> visited = new HashSet<>();
        dfsVisit(source, destination, visited, currentPath, allPaths, maxRoutes);
        return allPaths;
    }

    private void dfsVisit(Vertex<T> current, Vertex<T> destination, Set<Vertex<T>> visited,
                          List<Vertex<T>> currentPath, List<List<Vertex<T>>> allPaths, int maxRoutes) {
        visited.add(current);
        currentPath.add(current);

        if (current.equals(destination)) { //did we reach destination
            allPaths.add(new ArrayList<>(currentPath));
        } else if (allPaths.size() < maxRoutes) { // if total paths not greater than maxroutes
            for (Edge<T> edge : current.getEdges()) {
                if (!visited.contains(edge.getDestination())) {
                    dfsVisit(edge.getDestination(), destination, visited, currentPath, allPaths, maxRoutes); //recursively call dfs
                }
            }
        }

        visited.remove(current); //backtrack and explore other path
        currentPath.remove(currentPath.size() - 1);
    }
    public List<Vertex<T>> getVertices() {
        return vertices;
    }
}