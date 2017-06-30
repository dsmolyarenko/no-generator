package org.no.generator.configuration.util;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.interpolation.AkimaSplineInterpolator;
import org.apache.commons.math3.analysis.interpolation.DividedDifferenceInterpolator;
import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.interpolation.LoessInterpolator;
import org.apache.commons.math3.analysis.interpolation.NevilleInterpolator;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;
import org.apache.commons.math3.fraction.Fraction;
import org.no.generator.Source;

public class SourceUtils {

    /**
     * Divided Difference algorithm.
     */
    public static final String DD = "DD";

    /**
     * Hiroshi Akima algorithm.
     */
    public static final String HA = "HA";

    /**
     * Linear Interpolating algorithm.
     */
    public static final String LN = "LN";

    /**
     * Local Regression algorithm.
     */
    public static final String LS = "LS";

    /**
     * Neville's algorithm.
     */
    public static final String NL = "NL";

    /**
     * Cubic Spline algorithm.
     */
    public static final String SP = "SP";

    private static Map<String, Class<?>> interpolators;
    static {
        interpolators = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        interpolators.put(DD, DividedDifferenceInterpolator.class);
        interpolators.put(HA, AkimaSplineInterpolator.class);
        interpolators.put(LN, LinearInterpolator.class);
        interpolators.put(LS, LoessInterpolator.class);
        interpolators.put(NL, NevilleInterpolator.class);
        interpolators.put(SP, SplineInterpolator.class);
    }

    public static Source decorate(Source s, String it, double... distribution) {
        if (it != null && distribution != null && distribution.length > 0) {
            Class<?> ic = interpolators.get(it);
            if (ic == null) {
                throw new IllegalArgumentException("Unregistered interpolation type: " + it);
            }
            UnivariateInterpolator ui;
            try {
                ui = (UnivariateInterpolator) ic.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new IllegalStateException(); // should never happen
            }
            UnivariateFunction f = ui.interpolate(range(distribution), distribution);
            return () -> {
                while (true) {
                    double v = s.nextDouble();
                    double p = s.nextDouble();
                    if (p < f.value(v)) {
                        return v;
                    }
                }
            };
        }
        return s;
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

}
