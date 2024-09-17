package com.routefinder.routefinder;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.util.Pair;

import java.io.*;
import java.util.*;

public class ParisController {

    @FXML
    private ImageView mapImageView;

    @FXML
    private BorderPane imageBorderPane;

    @FXML
    private ComboBox<String> routeSourceComboBox, routeDestinationComboBox, methodComboBox, routesComboBox;

    private Graph graph = new Graph();
    private ImageGraph imageGraph;

    @FXML
    private Label maxRoutesLabel;

    @FXML
    private TextField maxRoutesTextField;

    @FXML
    private ListView<String> routeListView;

    List<List<Vertex<Landmark>>> paths;

    @FXML
    private void initialize() {
        Image image = new Image(String.valueOf(getClass().getResource("/com/routefinder/routefinder/parismap.png")));
        mapImageView.setImage(image);
        Image bwImage = new Image(String.valueOf(getClass().getResource("/com/routefinder/routefinder/bwparis.png")));
        this.imageGraph = new ImageGraph(bwImage);
        methodComboBox.getItems().add("Shortest Route (BFS)");
        methodComboBox.getItems().add("Shortest Route (Dijkstra)");
        methodComboBox.getItems().add("Most cultural route");
        methodComboBox.getItems().add("Depth First Search");
        methodComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("Depth First Search")) {
                maxRoutesLabel.setOpacity(1);
                maxRoutesTextField.setOpacity(1);
            } else {
                maxRoutesLabel.setOpacity(0);
                maxRoutesTextField.setOpacity(0);
            }
        });
        routesComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            List<Vertex<Landmark>> route = paths.get(Integer.parseInt(newValue.substring(6)) - 1);
            listRoute(route);
            drawRoute(route);

        });
        loadLandmarks();
        loadRoads();
    }

    public void loadLandmarks() {
        String landmarksPath = "src/main/resources/com/routefinder/routefinder/landmarks.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(landmarksPath))) {
            String line;
            br.readLine(); // Skip header line
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0];
                double xCoord = Double.parseDouble(parts[1]);
                double yCoord = Double.parseDouble(parts[2]);
                double cultureLevel = Double.parseDouble(parts[3]);


                Landmark landmark = new Landmark(name, xCoord, yCoord, cultureLevel);
                Vertex<Landmark> vertex = new Vertex<>(landmark);
                graph.addVertex(vertex);


                if (landmark.getName().length() > 3) {
                    routeSourceComboBox.getItems().add(landmark.getName());
                    routeDestinationComboBox.getItems().add(landmark.getName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadRoads() {
        String roadsPath = "src/main/resources/com/routefinder/routefinder/roads.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(roadsPath))) {
            String line;
            br.readLine(); // Skip header line
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String sourceName = parts[0];
                String destinationName = parts[1];
                double distance = Double.parseDouble(parts[2]);
                String streetName = parts[3];

                Vertex<Landmark> source = graph.findVertexByName(sourceName);
                Vertex<Landmark> destination = graph.findVertexByName(destinationName);
                if (source != null && destination != null) {
                    double culturalWeight = (distance + 1) * (destination.getData().getCultureLevel());
                    graph.addEdge(source, destination, distance, streetName, culturalWeight);
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }


    private void drawLandmark(Landmark landmark) {
        Circle circle = new Circle(landmark.getXCoord(), landmark.getYCoord(), 5, Color.RED);
        imageBorderPane.getChildren().add(circle);
    }

    @FXML
    public void routeAnalyser() {
        Landmark source = null;
        Landmark destination = null;
        source = getLandmarkByName(routeSourceComboBox.getSelectionModel().getSelectedItem());
        destination = getLandmarkByName(routeDestinationComboBox.getSelectionModel().getSelectedItem());
        Vertex<Landmark> sourceVertex = graph.findVertexByName(source.getName());
        Vertex<Landmark> destinationVertex = graph.findVertexByName(destination.getName());
        if (methodComboBox.getSelectionModel().getSelectedItem() == null) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Route Method not selected");
            errorAlert.setContentText("Please select a routing method");
            errorAlert.showAndWait();
        } else if (methodComboBox.getSelectionModel().getSelectedItem().equals("Shortest Route (BFS)")) {
            routesComboBox.setOpacity(0);
            List<int[]> path = imageGraph.bfs(
                    (int) source.getXCoord(),
                    (int) source.getYCoord(),
                    (int) destination.getXCoord(),
                    (int) destination.getYCoord());


            drawBFSRoute(path);
            drawLandmark(source);
            drawLandmark(destination);

        }
        if (methodComboBox.getSelectionModel().getSelectedItem().equals("Shortest Route (Dijkstra)")) {
            routesComboBox.setOpacity(0);
            List<Vertex<Landmark>> path = graph.dijkstra(sourceVertex, destinationVertex);
            drawRoute(path);
            listRoute(path);
            drawLandmark(source);
            drawLandmark(destination);

        }
        if (methodComboBox.getSelectionModel().getSelectedItem().equals("Most cultural route")) {
            routesComboBox.setOpacity(0);
            List<Vertex<Landmark>> path = graph.dijkstraCultural(sourceVertex, destinationVertex);
            drawRoute(path);
            listRoute(path);
            for (Vertex<Landmark> landmark : path) {
                if (landmark.getData().getName().length() > 3)
                    drawLandmark(landmark.getData());
            }

            drawLandmark(destination);

        }
        if (methodComboBox.getSelectionModel().getSelectedItem().equals("Depth First Search")) {
            routesComboBox.getItems().clear();
            paths = graph.dfsPaths(sourceVertex, destinationVertex, Integer.parseInt(maxRoutesTextField.getText()));
            routesComboBox.setOpacity(1);
            for (int i = 0; i < paths.size(); i++) {
                routesComboBox.getItems().add("Route " + (i + 1));
            }
            routesComboBox.getSelectionModel().selectFirst();
            drawLandmark(source);
            drawLandmark(destination);
        }
    }

    private Landmark getLandmarkByName(String landmarkName) {
        List<Vertex<Landmark>> vertices = graph.getVertices();
        for (Vertex<Landmark> vertex : vertices) {
            if (vertex.getData().getName().equals(landmarkName)) {
                return vertex.getData();
            }
        }
        return null;
    }

    private void listRoute(List<Vertex<Landmark>> path) {
        routeListView.getItems().clear();
        routeListView.getItems().add("Start: " + path.get(0).getData().getName());
        for (int i = 0; i < path.size() - 1; i++) {
            for (Edge edge : path.get(i).getEdges()) {
                if (edge.getDestination().equals(path.get(i + 1))) {
                    routeListView.getItems().add(edge.getStreetName() + ", " + edge.getWeight() + "km");
                }
            }
        }
        routeListView.getItems().add("Destination: " + path.get(path.size() - 1).getData().getName());
    }


    private void drawBFSRoute(List<int[]> path) {
        imageBorderPane.getChildren().removeIf(node -> node instanceof Line);
        imageBorderPane.getChildren().removeIf(node -> node instanceof Circle);
        for (int i = 0; i < path.size() - 1; i++) {
            int[] from = path.get(i);
            int[] to = path.get(i + 1);

            Line line = new Line(from[0], from[1], to[0], to[1]);
            line.setStrokeWidth(2);
            line.setStroke(Color.BLUE);
            imageBorderPane.getChildren().add(line);
        }
    }

    private void drawRoute(List<Vertex<Landmark>> path) {
        imageBorderPane.getChildren().removeIf(node -> node instanceof Line);
        imageBorderPane.getChildren().removeIf(node -> node instanceof Circle);

        for (int i = 0; i < path.size() - 1; i++) {
            Landmark from = path.get(i).getData();
            Landmark to = path.get(i + 1).getData();

            Line line = new Line(from.getXCoord(), from.getYCoord(), to.getXCoord(), to.getYCoord());
            line.setStrokeWidth(2);
            line.setStroke(Color.BLUE);
            imageBorderPane.getChildren().add(line);
        }
    }

}