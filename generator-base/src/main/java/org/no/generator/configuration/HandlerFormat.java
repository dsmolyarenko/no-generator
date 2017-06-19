package org.no.generator.configuration;

import static org.no.generator.configuration.util.ObjectUtils.transform;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Locale;

import org.no.generator.Generator;
import org.no.generator.configuration.context.DependencyContext;

public final class HandlerFormat extends ConfigurationHandlerDefault<Generator, HandlerFormat.Configuration> {

    @Override
    protected Generator create(Configuration c, DependencyContext context) {

        Generator[] arguments = transform(c.arguments, t -> context.get(Generator.class, t), Generator.class);

        MessageFormat mf = (c.locale == null)
            ? new MessageFormat(c.format)
            : new MessageFormat(c.format, c.locale);

        return a -> a.append(mf.format(transform(arguments, t -> {
            StringBuilder sb = new StringBuilder();
            try {
                t.append(sb);
            } catch (IOException e) {
                throw new IllegalStateException();
            }
            return sb.toString();
        }, Object.class)));
    }

    public static final class Configuration {

        private String format;

        private Locale locale;

        private Object[] arguments;

    }
}
