package org.no.generator.configuration;

import org.no.generator.Generator;
import org.no.generator.Source;
import org.no.generator.configuration.context.DependencyContext;

public final class HandlerRandomInteger extends ConfigurationHandlerDefault<Generator, HandlerRandomInteger.Configuration> {

    @Override
    protected Generator create(Configuration c, DependencyContext context) {

        if (c.count < 1) {
            throw new ConfigurationException();
        }

        Source source = context.get(Source.class, c.source);

        return a -> a.append(Integer.toString(c.shift + source.nextInt(c.count)));
    }

    public static final class Configuration {

        private Object source;

        private int shift;

        private int count = 32768;

    }

}
