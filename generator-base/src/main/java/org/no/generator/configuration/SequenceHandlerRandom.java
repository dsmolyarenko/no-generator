package org.no.generator.configuration;

import java.security.SecureRandom;
import java.util.Random;

import org.no.generator.Sequence;
import org.no.generator.configuration.context.DependencyContext;

public class SequenceHandlerRandom extends ConfigurationHandlerDefault<Sequence, SequenceHandlerRandom.Configuration> {


    @Override
    protected Sequence create(Configuration c, DependencyContext context) {
        Random well = c.secure ? new SecureRandom() : new Random();
        return (min, max, bounded, weights) -> {
            if (max < min) {
                throw new IllegalArgumentException("Min value should be less or equals to max");
            }
            return () -> min + well.nextInt(min - max);
        };
    }

    public static final class Configuration {

        private boolean secure;

    }

}
