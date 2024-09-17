//package com.routefinder.routefinder;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Stack;
//
//public class DepthFirstSearch {
//    private Graph<Landmark> graph;
//    private Vertex<Landmark> source;
//    private Vertex<Landmark> destination;
//    private List<Vertex<Landmark>> path;
//    private boolean pathFound;
//
//    public DepthFirstSearch(Graph<Landmark> graph, Vertex<Landmark> source, Vertex<Landmark> destination) {
//        this.graph = graph;
//        this.source = source;
//        this.destination = destination;
//        this.path = new ArrayList<>();
//        this.pathFound = false;
//    }
//
//    public List<Vertex<Landmark>> findPath() {
//        // Reset visited status of all vertices
//        for (Vertex<Landmark> vertex : graph.getVertices()) {
//            vertex.setVisited(false);
//        }
//        // Initialize the DFS
//        Stack<Vertex<Landmark>> stack = new Stack<>();
//        stack.push(source);
//
//        while (!stack.empty()) {
//            Vertex<Landmark> current = stack.pop();
//            if (!current.isVisited()) {
//                current.setVisited(true);
//                path.add(current);
//
//                if (current.equals(destination)) {
//                    pathFound = true;
//                    return path;
//                }
//
//                for (Edge<Landmark> edge : current.getNeighbors()) {
//                    if (!edge.getDestination().isVisited()) {
//                        stack.push(edge.getDestination());
//                    }
//                }
//            }
//        }
//
//        return pathFound ? path : null;
//    }
//}