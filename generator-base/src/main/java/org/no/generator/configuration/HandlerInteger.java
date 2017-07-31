package org.no.generator.configuration;

import java.util.function.Supplier;

import org.no.generator.Sequence;
import org.no.generator.Writer;
import org.no.generator.configuration.context.DependencyContext;

public final class HandlerInteger extends ConfigurationHandlerDefault<Writer, HandlerInteger.Configuration> {

    @Override
    protected Writer create(Configuration c, DependencyContext context) {
        if (c.sequence == null) {
            throw new ConfigurationException("Field `sequence` should be specified");
        }
        Sequence sequence = context.get(Sequence.class, c.sequence);

        Supplier<Integer> s = sequence.create(c.min, c.max, false, null);
        return a -> {
            a.append(Integer.toString(s.get()));
        };
    }

    public static final class Configuration {

        private Object sequence;

        private int min;

        private int max;

        private int step = 1;

        private int next;

    }
}
