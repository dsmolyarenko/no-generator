package org.no.generator.configuration;

import org.no.generator.Generator;
import org.no.generator.configuration.context.DependencyContext;

public final class HandlerSize extends ConfigurationHandlerDefault<Generator, HandlerSize.Configuration> {

    @Override
    protected Generator create(Configuration c, DependencyContext context) {

        Generator generator = context.get(Generator.class, c.sample);

        StringBuilder sb = new StringBuilder();
        return a -> {
            sb.setLength(0);
            generator.append(sb);
            a.append(Integer.toString(sb.length()));
        };
    }

    public static final class Configuration {

        private Object sample;

    }

}
