package org.no.generator.configuration;

import static org.no.generator.configuration.util.ObjectUtils.transform;

import org.no.generator.Generator;
import org.no.generator.Generators;
import org.no.generator.configuration.context.DependencyContext;

public final class HandlerLoop extends ConfigurationHandlerDefault<Generator, HandlerLoop.Configuration> {

    @Override
    protected Generator create(Configuration c, DependencyContext context) {
        return Generators.createRound(transform(c.samples, t -> {
            return context.get(Generator.class, t);
        }, Generator.class));
    }

    public static final class Configuration {

        private Object[] samples;

    }

}