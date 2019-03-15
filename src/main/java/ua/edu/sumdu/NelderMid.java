package ua.edu.sumdu;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import static java.lang.Math.sqrt;

public class NelderMid {
    public static Point count(Function function) {
        double alpha = 1d;  // reflection
        double beta = 0.5d; // compression
        double gamma = 2d;  // stretching
        int h = 2;          // ?
        double eps = 1e-1;
        double delta1 = delta1(function.getDimension(), h);
        double delta2 = delta2(function.getDimension(), h);
        Point firstApproximation = new Point(0, 0);
        Point [] points = basicSimplexPoints(firstApproximation, delta1, delta2);
        System.out.println("First points are:");
        for (Point point : points) {
            System.out.println(point);
        }
        while (!converged(points, function, alpha, beta, gamma, eps)) {
            System.out.println("Current the smallest F(" + points[0] + ") = " + function.count(points[0]));
        }
        Arrays.sort(points, new PointsComparator(function));
        return points[0];
    }

    private static double delta1(int n, int h) {
        return ((sqrt(n+1) + n - 1)/(sqrt(2) * n)) * h;
    }

    private static double delta2(int n, int h) {
        return ((sqrt(n+1) - 1)/(sqrt(2) * n)) * h;
    }

    private static Point [] basicSimplexPoints(Point firstApproximation, double delta1, double delta2) {
        Point [] result = new Point[firstApproximation.getDimension() + 1];
        for (int i = 1; i < result.length; i++) {
            double [] iPoint = new double[firstApproximation.getDimension()];
            for (int j = 0; j < firstApproximation.getDimension(); j++) {
                iPoint[j] = firstApproximation.getCoordinates()[j] + (i - 1 == j ? delta2 : delta1);
            }
            result[i] = new Point(iPoint);
        }
        result[0] = firstApproximation;
        return result;
    }

    private static Point countCG(Point [] points) {
        double[] coordinates = new double[points[0].getDimension()];
        for (int j = 0; j < points.length - 1; j++) {
            for (int i = 0; i < coordinates.length; i++) {
                coordinates[j] += points[i].getCoordinates()[j];
            }
        }
        for (int i = 0; i < coordinates.length; i++) {
            coordinates[i] /= points.length;
        }
        return new Point(coordinates);
    }

    private static class PointsComparator implements Comparator<Point> {
        private final Function function;

        public PointsComparator(Function function) {
            this.function = function;
        }

        @Override
        public int compare(Point o1, Point o2) {
            if (o1.getDimension() != o2.getDimension()) {
                throw new IllegalArgumentException("Points must have the save dimension to be able to be compared");
            }
            return Double.compare(function.count(o1), function.count(o2));
        }
    }

    private static Point reflection(Point reflected, Point base, double alpha) {
        if (reflected.getDimension() != base.getDimension()) {
            throw new IllegalArgumentException("Points have different coordinates set");
        }
        double [] pointCoordinates = new double[reflected.getDimension()];
        for (int i = 0; i < reflected.getDimension(); i++) {
            pointCoordinates[i] = (1+alpha)*base.getCoordinates()[i]-alpha*reflected.getCoordinates()[i];
        }
        Point htp = new Point(pointCoordinates);
        System.out.println("The new reflected point is " + htp);
        return htp;
    }

    private Point getNewPoint (Point [] points, Function function) {
        /*Arrays.sort(points, new PointsComparator(function));
        Point cg = countCG(points, function);
        if*/
        return null;
    }

    private static boolean converged(Point [] points, Function function, double alpha, double beta, double gamma, double eps) {
        Arrays.sort(points, new PointsComparator(function));
        Point xc = countCG(points);
        double Fh = function.count(points[points.length - 1]);
        double Fr = function.count(reflection(points[points.length - 1], xc, alpha));
        double Fl = function.count(points[0]);
        double Fg = function.count(points[points.length / 2]);
        if (Fr < Fl) {
            Point xe = stretching(reflection(points[points.length - 1], xc, alpha), xc, gamma);
            double Fe = function.count(xe);
            if (Fe < Fr) {
                points[points.length - 1] = xe;
            } else {
                points[points.length - 1] = reflection(points[points.length - 1], xc, alpha);
            }
            return estimateConvergence(points, function, eps);
        } else if (Fl < Fr && Fr < Fg) {
            points[points.length - 1] = reflection(points[points.length - 1], xc, alpha);
            return estimateConvergence(points, function, eps);
        } else if (Fg < Fr && Fr < Fh) {
            points[points.length - 1] = reflection(points[points.length - 1], xc, alpha);
            Point xs = compressing(points[points.length - 1], xc, beta);
            double Fs = function.count(xs);
            if (Fs < Fh) {
                points[points.length - 1] = xs;
            }
            return estimateConvergence(points, function, eps);
        } else if (Fh < Fr) {
            Point xs = compressing(points[points.length - 1], xc, beta);
            double Fs = function.count(xs);
            if (Fs < Fh) {
                points[points.length - 1] = xs;
            } else {
                for (int i = 1; i < points.length; i++) {
                    points[i] = homotheticalTransformation(points[i], points[0]);
                }
            }
            return estimateConvergence(points, function, eps);
        } else { // in case if function has the same value in the xr that we already have among the points
            System.err.println("F=F");
            return estimateConvergence(points, function, eps);
        }
    }

    private static boolean estimateConvergence(Point [] points, Function function, double eps) {
        double [] F = new double[points.length];
        for (int i = 0 ; i < points.length; i++) {
            F[i] = function.count(points[i]);
        }
        return sqrt(ems(F)) < eps;
    }

    private static double ems(double [] numbers) {
        double expected = expected(numbers);
        double sum = 0;
        for (double number : numbers) {
            sum += Math.pow(expected - number, 2);
        }
        return sum / numbers.length;
    }

    private static double expected(double [] numbers) {
        double sum = 0;
        for (double number : numbers) {
            sum += number;
        }
        return sum / numbers.length;
    }

    private static Point homotheticalTransformation (Point transformed, Point base) {
        if (transformed.getDimension() != base.getDimension()) {
            throw new IllegalArgumentException("Points have different coordinates set");
        }
        double [] resultCoordinates = new double[transformed.getCoordinates().length];
        for (int i = 0; i < resultCoordinates.length; i++) {
            resultCoordinates[i] = base.getCoordinates()[i] + ((transformed.getCoordinates()[i] - base.getCoordinates()[i]) / 2);
        }
        return new Point(resultCoordinates);
    }

    private static Point stretching(Point stretched, Point base, double gamma) {
        if (stretched.getDimension() != base.getDimension()) {
            throw new IllegalArgumentException("Points have different coordinates set");
        }
        double [] pointCoordinates = new double[stretched.getDimension()];
        for (int i = 0; i < stretched.getDimension(); i++) {
            pointCoordinates[i] = (1 - gamma) * base.getCoordinates()[i] + gamma * stretched.getCoordinates()[i];
        }
        Point result = new Point(pointCoordinates);
        System.out.println("The new stretched point is " + result);
        return result;
    }

    private static Point compressing(Point compressed, Point base, double beta) {
        if (compressed.getDimension() != base.getDimension()) {
            throw new IllegalArgumentException("Points have different coordinates set");
        }
        double [] pointCoordinates = new double[compressed.getDimension()];
        for (int i = 0; i < compressed.getDimension(); i++) {
            pointCoordinates[i] = beta * compressed.getCoordinates()[i] + (1 - beta) * base.getCoordinates()[i];
        }
        Point result = new Point(pointCoordinates);
        System.out.println("The new compressed point is " + result);
        return result;
    }
}