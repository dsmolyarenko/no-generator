package org.no.generator.configuration;

import org.no.generator.Generator;
import org.no.generator.configuration.context.DependencyContext;

public final class HandlerSequenceString extends ConfigurationHandlerDefault<Generator, HandlerSequenceString.Configuration> {

    @Override
    protected Generator create(Configuration c, DependencyContext context) {

        if (c.min >= c.max) {
            throw new IllegalArgumentException("Max character should go after min in the character table: " + c.min);
        }

        // create initial buffer
        char[] buffer = new char[c.length];
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = c.min;
        }

        return a -> {
            a.append(new String(buffer));

            //
            // roll characters
            //

            int position = 0;
            while (true) {
                int index = buffer.length - 1 - position; // inverse order
                char v = buffer[index];
                if (v < c.max) {
                    buffer[index] = ++v;
                    break;
                }
                buffer[index] = c.min;
                if (++position == buffer.length) {
                    position = 0;
                }
            }
        };
    }

    public static final class Configuration {

        private int length = 16;

        private char min = 'A';

        private char max = 'Z';

    }
}
