package org.no.generator.configuration;

import org.no.generator.Generator;
import org.no.generator.Source;
import org.no.generator.configuration.context.DependencyContext;

public final class HandlerRandomStringSet extends ConfigurationHandlerDefault<Generator, HandlerRandomStringSet.Configuration> {

    @Override
    protected Generator create(Configuration c, DependencyContext context) {

        if (c.samples == null || c.samples.length == 0) {
            throw new ConfigurationException("Property samples is not defined or empty");
        }

        Source source = context.get(Source.class, c.source);

        return a -> a.append(c.samples[source.nextInt(c.samples.length)]);
    }

    public static final class Configuration {

        private Object source;

        private String[] samples;

    }

}
