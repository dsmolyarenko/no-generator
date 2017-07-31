package org.no.generator.configuration;

import org.no.generator.Writer;
import org.no.generator.Generators;
import org.no.generator.configuration.context.DependencyContext;

public final class HandlerText extends ConfigurationHandlerDefault<Writer, HandlerText.Configuration> {

    @Override
    protected Writer create(Configuration c, DependencyContext context) {
        return Generators.createString(c.text);
    }

    public static final class Configuration {

        private String text;

    }

}
