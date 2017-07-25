package org.no.generator.configuration;

import static org.no.generator.configuration.util.ObjectUtils.map;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.no.generator.Generator;
import org.no.generator.Generators;
import org.no.generator.configuration.context.DependencyContext;
import org.no.generator.configuration.util.StringUtils;

public final class HandlerTemplate extends ConfigurationHandlerDefault<Generator, HandlerTemplate.Configuration> {

    @Override
    protected Generator create(Configuration c, DependencyContext context) {

        if (c.template == null) {
            throw new ConfigurationException("Property `template` is not defined");
        }

        if (c.samples == null) {
            throw new ConfigurationException("Property `samples` is not defined");
        }

        Generator[] samples = map(c.samples, t -> {
            return context.get(Generator.class, t);
        }, Generator.class);

        List<Generator> sequence = new ArrayList<>();

        StringUtils.split(pattern, c.template, v -> sequence.add(a -> a.append(v)), v -> sequence.add(samples[Integer.valueOf(v) % samples.length]));

        return Generators.createUnion(sequence.toArray(new Generator[sequence.size()]));
    }

    public static final class Configuration {

        private String template;

        private Object[] samples;

        public Configuration setTemplate(String template) {
            this.template = template;
            return this;
        }

        public Configuration setSamples(Object... samples) {
            this.samples = samples;
            return this;
        }
    }

    public static Configuration configuration() {
        return new Configuration();
    }

    private static Pattern pattern = Pattern.compile("(\\{(\\d)+\\})");
}
