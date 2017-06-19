package org.no.generator.configuration;

import static org.no.generator.configuration.util.ObjectUtils.transform;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

import org.no.generator.Generator;
import org.no.generator.configuration.context.DependencyContext;
import org.no.generator.configuration.util.StringUtils;

public final class HandlerTemplate extends ConfigurationHandlerDefault<Generator, HandlerTemplate.Configuration> {

    @Override
    protected Generator create(Configuration c, DependencyContext context) {

        Generator[] arguments = transform(c.arguments, new Function<Object, Generator>() {
            @Override
            public Generator apply(Object t) {
                return context.get(Generator.class, t);
            }
        }, Generator.class);

        List<Generator> sequence = new ArrayList<>();

        StringUtils.split(pattern, c.template, v -> sequence.add(a -> a.append(v)), v -> sequence.add(arguments[Integer.valueOf(v)]));

        return a -> {
            for (Generator generator : sequence) {
                generator.append(a);
            }
        };
    }

    public static final class Configuration {

        private String template;

        private Object[] arguments;

    }

    private static Pattern pattern = Pattern.compile("(\\{(\\d)+\\})");
}
