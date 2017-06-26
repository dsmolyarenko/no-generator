package org.no.generator.configuration.util;

import java.io.ByteArrayInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.commons.math3.fraction.Fraction;
import org.no.generator.Source;

public class SourceUtils {

    public static Source decorate(Source s, double... distribution) {
        if (distribution != null && distribution.length > 0) {
            PolynomialSplineFunction f = new LinearInterpolator().interpolate(range(distribution), normalize(distribution));
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

    public static final class BitInputStream extends FilterInputStream {

        private int rest;

        private int last;

        public BitInputStream(InputStream in) {
            super(in);
        }

        public int read(int bits) throws IOException {
            int b = bits;
            int r = 0;

            while (true) {
                if (b == 0) {
                    return r;
                }
                if (rest > 0) {
                    int t = Math.min(b, rest);
                    r += (last & ((1 << t) - 1)) << (bits - b);
                    b -= t;
                    last >>= t;
                    rest -= t;
                }
                if (b == 0) {
                    return r;
                }
                while (b >= 8) {
                    r <<= 8;
                    r += read() << (bits - b);
                    b -= 8;
                }
                if (b == 0) {
                    return r;
                }

                last = read();
                if (last == -1) {
                    throw new IOException();
                }
                rest = 8;
            }
        }
    }

    public static void main(String[] args) throws IOException {
//        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[] {
//                (byte) 0b11000111,
//                (byte) 0b01110001,
//                (byte) 0b00011100,
//        });

        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[] {
                (byte) 0b00000100,
                (byte) 0b00001001,
                (byte) 0b00000010,
        });

        BitInputStream bis = new BitInputStream(bais);

        System.out.println(bis.read(9));
        System.out.println(bis.read(9));
        System.out.println(bis.read(9));
        System.out.println(bis.read(9));
        System.out.println(bis.read(6));
        System.out.println(bis.read(6));
        System.out.println(bis.read(6));
        System.out.println(bis.read(6));
        System.out.println(bis.read(6));
        System.out.println(bis.read(6));
        System.out.println(bis.read(6));
    }

}
