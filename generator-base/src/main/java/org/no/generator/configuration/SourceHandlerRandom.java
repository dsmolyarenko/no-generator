package org.no.generator.configuration;

import java.security.SecureRandom;
import java.util.Random;

import org.no.generator.Source;
import org.no.generator.configuration.context.DependencyContext;
import org.no.generator.configuration.util.SourceUtils;

public class SourceHandlerRandom extends ConfigurationHandlerDefault<Source, SourceHandlerRandom.Configuration> {

    @Override
    protected Source create(Configuration c, DependencyContext context) {
        return SourceUtils.decorate((c.secure ? new SecureRandom() : new Random())::nextDouble, c.di, c.distribution);
    }

    public static final class Configuration {

        private boolean secure;

        private double[] distribution;

        private String di = SourceUtils.LN;

    }

}
