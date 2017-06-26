package org.no.generator.configuration;

import org.no.generator.Generator;
import org.no.generator.configuration.context.DependencyContext;

public final class HandlerJoin extends ConfigurationHandlerDefault<Generator, HandlerJoin.Configuration> {

    @Override
    protected Generator create(Configuration c, DependencyContext context) {

        Generator sample = context.get(Generator.class, c.sample);

        return a -> {
            if (c.prefix != null) {
                a.append(c.prefix);
            }

            int count = c.number;

            for (int i = 0; i < count; i++) {
                if (Thread.interrupted()) {
                    break;
                }
                if (i != 0) {
                    if (c.delimiter != null) {
                        a.append(c.delimiter);
                    }
                }
                sample.append(a);
            }

            if (c.suffix != null) {
                a.append(c.suffix);
            }
        };
    }

    public static final class Configuration {

        private String prefix;

        private String suffix;

        String delimiter;

        private Object sample;

        private int number = 16;

    }

}
