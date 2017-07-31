package org.no.generator.configuration;

import org.no.generator.Writer;
import org.no.generator.Source;
import org.no.generator.configuration.context.DependencyContext;

public final class HandlerSource extends ConfigurationHandlerDefault<Writer, HandlerSource.Configuration> {

    @Override
    protected Writer create(Configuration c, DependencyContext context) {
        if (c.source == null) {
            throw new ConfigurationException("Field `source` should be specified");
        }
        Source source = context.get(Source.class, c.source);

        return a -> {
            final Object next = source.next();
            a.append(next.toString());
        };
    }

    public static final class Configuration {

        private Object source;

    }

}
