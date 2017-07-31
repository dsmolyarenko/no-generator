package org.no.generator.configuration;

import org.no.generator.Writer;
import org.no.generator.DataSourceExhaustedException;
import org.no.generator.configuration.context.DependencyContext;

public final class HandlerJoin extends ConfigurationHandlerDefault<Writer, HandlerJoin.Configuration> {

    @Override
    protected Writer create(Configuration c, DependencyContext context) {

        Writer sample = context.get(Writer.class, c.sample);

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
                try {
                    sample.append(a);
                } catch (DataSourceExhaustedException e) {
                    break;
                }
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

        private int number = Integer.MAX_VALUE;

    }

}
