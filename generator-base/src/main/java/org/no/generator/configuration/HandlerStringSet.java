package org.no.generator.configuration;

import java.util.function.Supplier;

import org.no.generator.Sequence;
import org.no.generator.Writer;
import org.no.generator.configuration.context.DependencyContext;

public final class HandlerStringSet extends ConfigurationHandlerDefault<Writer, HandlerStringSet.Configuration> {

    @Override
    protected Writer create(Configuration c, DependencyContext context) {

        if (c.samples == null) {
            throw new ConfigurationException("Field `samples` is not defined");
        }

        if (c.sequence == null) {
            throw new ConfigurationException("Field `sequence` should be specified");
        }
        Sequence sequence = context.get(Sequence.class, c.sequence);

        Supplier<Integer> s = sequence.create(0, c.samples.length, false, c.weights);
        return a -> {
            a.append(c.samples[s.get()]);
        };
    }

    public static final class Configuration {

        private Object sequence;

        private String[] samples;

        private double[] weights;

    }

}
