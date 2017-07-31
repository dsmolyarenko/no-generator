package org.no.generator.configuration;

import org.no.generator.Writer;
import org.no.generator.configuration.context.DependencyContext;

public final class HandlerSize extends ConfigurationHandlerDefault<Writer, HandlerSize.Configuration> {

    @Override
    protected Writer create(Configuration c, DependencyContext context) {

        Writer generator = context.get(Writer.class, c.sample);

        StringBuilder sb = new StringBuilder();
        return a -> {
            sb.setLength(0);
            generator.append(sb);
            a.append(Integer.toString(sb.length()));
        };
    }

    public static final class Configuration {

        private Object sample;

    }

}
