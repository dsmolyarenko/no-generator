package org.no.generator.configuration;

import static org.no.generator.configuration.util.ObjectUtils.map;

import java.util.function.Supplier;

import org.no.generator.Sequence;
import org.no.generator.Source;
import org.no.generator.Writer;
import org.no.generator.configuration.context.DependencyContext;

public final class HandlerSwitch extends ConfigurationHandlerDefault<Writer, HandlerSwitch.Configuration> {

    @Override
    protected Writer create(Configuration c, DependencyContext context) {
        if (c.sequence == null) {
            throw new ConfigurationException("Field `sequence` should be specified");
        }
        Sequence sequence = context.get(Sequence.class, c.sequence);

        if (c.samples != null && c.sources != null || c.samples == null && c.sources == null) {
            throw new ConfigurationException("Either `samples` or `sources` needs to be defined");
        }

        if (c.samples != null) {
            Writer[] writers = map(c.samples, t -> context.get(Writer.class, t), Writer.class);

            Supplier<Integer> s = sequence.create(0, writers.length, false, c.weights);
            return (a) -> {
                writers[s.get()].append(a);
            };
        }

        if (c.sources != null) {
            Source[] sources = map(c.sources, t -> context.get(Source.class, t), Source.class);

            Supplier<Integer> s = sequence.create(0, sources.length, false, c.weights);
            return (a) -> {
                a.append(sources[s.get()].toString());
            };
        }

        return null; // impossible case
    }

    public static final class Configuration {

        private Object sequence;

        private Object[] samples;

        private Object[] sources;

        private double[] weights;

    }

}
