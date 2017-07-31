package org.no.generator;

import java.util.function.Supplier;

public interface Sequence {

    Supplier<Integer> create(int min, int max, boolean bounded, double... weights);

}
