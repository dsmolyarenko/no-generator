package org.no.generator.configuration;

import org.no.generator.Generator;
import org.no.generator.configuration.context.DependencyContext;
import org.no.generator.util.MutableInt;

public final class HandlerSequenceInteger extends ConfigurationHandlerDefault<Generator, HandlerSequenceInteger.Configuration> {

    @Override
    protected Generator create(Configuration c, DependencyContext context) {
        MutableInt mutableInt = new MutableInt(c.next);
        mutableInt.setMin(c.min);
        mutableInt.setMax(c.max);
        return a -> {
            a.append(Long.toString(mutableInt.getAndAdd(c.step)));
        };
    }

    public static final class Configuration {

        private int min;

        private int max;

        private int step = 1;

        private int next;

    }
}
