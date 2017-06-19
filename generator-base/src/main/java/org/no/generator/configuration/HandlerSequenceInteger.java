package org.no.generator.configuration;

import java.io.IOException;

import org.no.generator.Generator;
import org.no.generator.configuration.context.DependencyContext;

public final class HandlerSequenceInteger extends ConfigurationHandlerDefault<Generator, HandlerSequenceInteger.Configuration> {

    @Override
    protected Generator create(Configuration c, DependencyContext context) {

        return new Generator() {

            private int sequence; {
                if (c.seqIntMin != null) {
                    sequence = c.seqIntMin;
                }
            }

            @Override
            public void append(Appendable a) throws IOException {
                a.append(Integer.toString(sequence++));

                if (c.seqIntMax != null) {
                    if (sequence > c.seqIntMax) {
                        if (c.seqIntMin != null) {
                            sequence = c.seqIntMin;
                        } else {
                            sequence = 0;
                        }
                    }
                }
            }
        };
    }

    public static final class Configuration {

        private Integer seqIntMin;

        private Integer seqIntMax;

    }
}
