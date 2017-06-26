package org.no.generator.configuration;

import org.no.generator.Generator;
import org.no.generator.Source;
import org.no.generator.configuration.context.DependencyContext;

public final class HandlerRandomSet extends ConfigurationHandlerDefault<Generator, HandlerRandomSet.Configuration> {

    @Override
    protected Generator create(Configuration c, DependencyContext context) {

        Source source = context.get(Source.class, c.source);

        return a -> a.append(c.samples[source.nextInt(c.samples.length)]);
    }

    public static final class Configuration {

        private Object source;

        private String[] samples;

    }

}