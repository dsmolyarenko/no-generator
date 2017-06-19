package org.no.generator;

import java.security.SecureRandom;

import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.RandomGeneratorFactory;
import org.apache.commons.math3.stat.StatUtils;

public class SourceUtils {

    public static Source createDefault(RandomGenerator r, double... distribution) {
        if (distribution != null && distribution.length > 0) {
            PolynomialSplineFunction f = new LinearInterpolator().interpolate(range(distribution), normalize(distribution));
            return () -> {
                while (true) {
                    double v = r.nextDouble();
                    double p = r.nextDouble();
                    if (p < f.value(v)) {
                        return v;
                    }
                }
            };
        }
        return () -> r.nextDouble();
    }

    public static Source createDefault(double... distribution) {
        return createDefault(RandomGeneratorFactory.createRandomGenerator(new SecureRandom()), distribution);
    }

    private static double[] range(double[] values) {
        int length = values.length;
        if (length < 2) {
            throw new IllegalArgumentException();
        }

        double[] result = new double[length];
        for (int i = 0; i < length; i++) {
            result[i] = new Fraction(i, length - 1).doubleValue();
        }
        return result;
    }

    private static double[] normalize(double[] values) {
        int length = values.length;
        if (length < 2) {
            throw new IllegalArgumentException();
        }

        double min = 0;
        double max = 0;
        for (double d : values) {
            min = Math.min(min, d);
            max = Math.max(max, d);
        }

        double[] result = new double[length];
        for (int i = 0; i < length; i++) {
            result[i] = values[i]; // TODO use weights
        }
        return result;
    }

    public static void main(String[] args) {
        double[] a = new double[] { 1, 2, 3 };
        double[] b = StatUtils.normalize(a);
        for (double d : b) {
            System.out.println(d);
        }
    }

}
