package com.routefinder.routefinder;

import java.util.List;
import java.util.ArrayList;
public class Edge<T> {
    private Vertex<T> destination;
    private double weight, culturalWeight;

    private String streetName;
    public Edge(Vertex<T> destination, double weight, String streetName, double culturalWeight) {
        this.destination = destination;
        this.weight = weight;
        this.streetName = streetName;
        this.culturalWeight = culturalWeight;
    }

    public Vertex<T> getDestination() {
        return destination;
    }

    public double getWeight() {
        return weight;
    }

    public String getStreetName() {
        return streetName;
    }

    public double getCulturalWeight() {
        return culturalWeight;
    }
}