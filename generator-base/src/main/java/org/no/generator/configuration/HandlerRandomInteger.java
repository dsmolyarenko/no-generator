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

        if (c.shift >= 0 && c.zeros) {
            int min = Integer.toString(c.shift).length();
            int max = Integer.toString(c.shift + c.count - 1).length();
            if (max - min > 0) {
                return a -> {
                    String value = Integer.toString(c.shift + source.nextInt(c.count));
                    for (int i = 0; i < max - value.length(); i++) {
                        a.append('0');
                    }
                    a.append(value);
                };
            }
        }
        return a -> a.append(Integer.toString(c.shift + source.nextInt(c.count)));
    }

    public static final class Configuration {

        private Object source;

        private boolean zeros;

        private int shift;

        private int count = 32768;

    }

}
