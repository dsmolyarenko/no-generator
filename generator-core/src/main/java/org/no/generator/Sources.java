package org.no.generator;

import java.util.Random;

public class Sources {

    public static Source createDefault() {
        Random random = new Random();
        return random::nextDouble;
    }

}
