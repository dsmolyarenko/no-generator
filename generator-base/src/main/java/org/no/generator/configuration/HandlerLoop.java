package org.no.generator.configuration;

import static org.no.generator.configuration.util.ObjectUtils.map;

import org.no.generator.Writer;
import org.no.generator.Generators;
import org.no.generator.configuration.context.DependencyContext;

public final class HandlerLoop extends ConfigurationHandlerDefault<Writer, HandlerLoop.Configuration> {

    @Override
    protected Writer create(Configuration c, DependencyContext context) {
        return Generators.createUnion(map(c.samples, t -> {
            return context.get(Writer.class, t);
        }, Writer.class));
    }

    public static final class Configuration {

        private Object[] samples;

    }

}
