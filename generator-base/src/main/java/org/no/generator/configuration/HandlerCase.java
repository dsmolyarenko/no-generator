package org.no.generator.configuration;

import java.util.HashMap;
import java.util.Map;

import org.no.generator.Generator;
import org.no.generator.GeneratorValueHolder;
import org.no.generator.configuration.HandlerCase.Configuration.Case;
import org.no.generator.configuration.context.DependencyContext;

public final class HandlerCase extends ConfigurationHandlerDefault<Generator, HandlerCase.Configuration> {

    @Override
    protected Generator create(Configuration c, DependencyContext context) {

        Generator g = context.get(Generator.class, c.sample);
        if (g.getClass() != GeneratorValueHolder.class) {
            throw new ConfigurationException("Property 'sample' should be held by parent context");
        }

        GeneratorValueHolder generatorValue = (GeneratorValueHolder) g;

        Map<String, Generator> cases = new HashMap<>();
        for (Case cs : c.cases) {
            cases.put(cs.value, context.get(Generator.class, cs.sample));
        }

        Generator generatorOthrwise = (c.otherwise != null)
                ? context.get(Generator.class, c.otherwise)
                : null;

        return a -> {
            Generator generator = cases.get(generatorValue.getValue());
            if (generator == null) {
                generator = generatorOthrwise;
            }
            if (generator != null) {
                generator.append(a);
            }
        };
    }

    public static final class Configuration {

        private Case[] cases;

        private Object sample;

        private Object otherwise;

        public static class Case {

            private String value;

            private Object sample;
        }
    }

}
