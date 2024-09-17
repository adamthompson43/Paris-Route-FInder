package com.routefinder.routefinder;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import java.util.*;

public class ImageGraph {
    private Map<Integer, List<Integer>> adjacencyList = new HashMap<>();
    private int width;
    private int height;

    public ImageGraph(Image image) {
        this.width = (int) image.getWidth();
        this.height = (int) image.getHeight();
        buildGraph(image);
    }

    private void buildGraph(Image image) {
        PixelReader reader = image.getPixelReader();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (isWhite(reader, x, y)) {
                    int index = y * width + x;
                    List<Integer> neighbors = getNeighbors(reader, x, y);
                    adjacencyList.put(index, neighbors);
                }
            }
        }
    }

    private List<Integer> getNeighbors(PixelReader reader, int x, int y) {
        List<Integer> neighbors = new ArrayList<>();
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

        for (int[] dir : directions) {
            int nx = x + dir[0];
            int ny = y + dir[1];
            if (nx >= 0 && nx < width && ny >= 0 && ny < height && isWhite(reader, nx, ny)) {
                neighbors.add(ny * width + nx);
            }
        }
        return neighbors;
    }

    private boolean isWhite(PixelReader reader, int x, int y) {
        Color color = reader.getColor(x, y);
        return !color.equals(Color.BLACK);  // check if the color is white
    }

    public List<int[]> bfs(int startX, int startY, int endX, int endY) {
        int start = startY * width + startX;
        int end = endY * width + endX;
        Queue<Integer> queue = new LinkedList<>(); //queue of vertices waiting to process
        Map<Integer, Integer> path = new HashMap<>(); //store previous vertex
        Set<Integer> visited = new HashSet<>(); // track visited vertices

        queue.add(start);
        visited.add(start);
        path.put(start, null);

        while (!queue.isEmpty()) {
            int current = queue.poll();

            if (current == end) {
                return reconstructPath(path, start, end);
            }

                for (int neighbor : adjacencyList.get(current)) {
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        queue.add(neighbor);
                        path.put(neighbor, current);
                    }
                }
        }
        return null; // no path found
    }

    private List<int[]> reconstructPath(Map<Integer, Integer> path, int start, int end) {
        LinkedList<int[]> route = new LinkedList<>();
        for (Integer step = end; step != null; step = path.get(step)) {
            route.addFirst(new int[]{step % width, step / width});
        }
        return route;
    }

}
