package org.no.generator;

public interface Source {

    public static final String DEFAULT = "default";

    double nextDouble();

    default int nextInt(int n) {
        return (int) (n * nextDouble());
    }

}
