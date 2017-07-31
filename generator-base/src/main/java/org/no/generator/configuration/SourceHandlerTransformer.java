package org.no.generator.configuration;

import static org.no.generator.configuration.util.ObjectUtils.get;
import static org.no.generator.util.SplitUtils.split;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import org.no.generator.Source;
import org.no.generator.configuration.context.DependencyContext;

public final class SourceHandlerTransformer extends ConfigurationHandlerDefault<Source, SourceHandlerTransformer.Configuration> {

    @Override
    protected Source create(Configuration c, DependencyContext context) {
        if (c.source == null) {
            throw new ConfigurationException("Field `source` should be specified");
        }
        Source source = context.get(Source.class, c.source);

        List<BiConsumer<StringBuilder, Object>> appenders = new ArrayList<>();
        split(c.format, (t) -> appenders.add((s, o) -> s.append(t)), (n, a) -> {
            if (n.equals("this")) {
                appenders.add((s, o) -> s.append(o));
            } else {
                appenders.add((s, o) -> s.append(get(o, n)));
            }
        });

        return () -> {
            Object o = source.next();

            StringBuilder s = new StringBuilder();
            for (BiConsumer<StringBuilder, Object> a : appenders) {
                a.accept(s, o);
            }
            return s.toString();
        };
    }

    public static final class Configuration {

        private Object source;

        private String format;

    }

}
