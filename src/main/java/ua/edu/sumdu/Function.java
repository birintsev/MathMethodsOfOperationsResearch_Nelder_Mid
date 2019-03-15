package ua.edu.sumdu;

public interface Function {
    double count(double...x) throws NelderMidException;
    default double count(Point point) {
        return count(point.getCoordinates());
    }
    int getDimension();
}
