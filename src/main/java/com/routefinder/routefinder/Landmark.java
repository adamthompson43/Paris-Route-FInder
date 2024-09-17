package com.routefinder.routefinder;

public class Landmark {
    private String name;
    private double xCoord, yCoord, cultureLevel;


    public Landmark(String name, double xCoord, double yCoord, double cultureLevel) {
        this.name = name;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.cultureLevel = cultureLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public void setXCoord(double xCoord) {
        this.xCoord = xCoord;
    }

    public double getXCoord() {
        return xCoord;
    }

    public void setYCoord(double yCoord) {
        this.yCoord = yCoord;
    }

    public double getYCoord() {
        return yCoord;
    }

    public double getCultureLevel() {
        return cultureLevel;
    }

    public void setCultureLevel(double cultureLevel) {
        this.cultureLevel = cultureLevel;
    }

    @Override
    public String toString() {
        return "Landmark{" +
                "name='" + name + '\'' +
                ", xCoord=" + xCoord +
                ", yCoord=" + yCoord +
                ", cultureLevel=" + cultureLevel +
                '}';
    }
}