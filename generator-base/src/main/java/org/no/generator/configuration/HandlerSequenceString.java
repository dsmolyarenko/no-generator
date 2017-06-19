package org.no.generator.configuration;

import org.no.generator.Generator;
import org.no.generator.configuration.context.DependencyContext;

public final class HandlerSequenceString extends ConfigurationHandlerDefault<Generator, HandlerSequenceString.Configuration> {

    @Override
    protected Generator create(Configuration c, DependencyContext context) {

        if (c.seqStrChrMin >= c.seqStrChrMax) {
            throw new IllegalArgumentException("Max character should go after min in the character table: " + c.seqStrChrMin);
        }

        // create initial buffer
        char[] buffer = new char[c.seqStrLen];
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = c.seqStrChrMin;
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
                if (v < c.seqStrChrMax) {
                    buffer[index] = ++v;
                    break;
                }
                buffer[index] = c.seqStrChrMin;
                if (++position == buffer.length) {
                    position = 0;
                }
            }
        };
    }

    public static final class Configuration {

        private int seqStrLen = 16;

        private char seqStrChrMin = 'A';

        private char seqStrChrMax = 'Z';

    }
}
