package ua.edu.sumdu;

import static java.lang.Math.pow;

public final class FunctionVar27Impl implements Function {
    @Override
    public double count(double... x) throws NelderMidException {
        if (x.length != 2) {
            throw new NelderMidException(new IllegalArgumentException("Wrong number of variables for this variant"));
        }
        return pow(x[0] - 1, 2) + 2 * pow(x[1], 2);
    }

    @Override
    public int getDimension() {
        return 2;
    }
}
