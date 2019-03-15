package ua.edu.sumdu;

import java.util.Arrays;

public class Point {
    private double[] coordinates;

    private String tag;

    public Point (double...coordinates) {
        if (coordinates.length == 0) {
            throw new IllegalArgumentException("At least one dimension is required");
        }
        this.coordinates = coordinates;
    }

    public int getDimension() {
        return coordinates.length;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(tag == null ? "Point" : tag).append(" (");
        for (double coordinate : coordinates) {
            stringBuilder.append(coordinate).append(';');
        }
        return new StringBuilder(stringBuilder.toString().substring(0, stringBuilder.length() - 1)).append(')').toString();
    }

    public double[] getCoordinates() {
        return coordinates;
    }
}
