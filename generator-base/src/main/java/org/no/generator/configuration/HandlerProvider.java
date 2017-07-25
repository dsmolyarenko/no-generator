package org.no.generator.configuration;

import org.no.generator.Generator;
import org.no.generator.Provider;
import org.no.generator.configuration.context.DependencyContext;

public final class HandlerProvider extends ConfigurationHandlerDefault<Generator, HandlerProvider.Configuration> {

    @Override
    protected Generator create(Configuration c, DependencyContext context) {

        Provider provider = context.get(Provider.class, c.provider);

        return a -> {
            Object o = provider.next();
            if (o != null) {
                a.append(o.toString());
            }
        };
    }

    public static final class Configuration {

        private Object provider;

    }

}
