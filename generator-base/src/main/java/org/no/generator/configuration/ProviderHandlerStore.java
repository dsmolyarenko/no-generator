package org.no.generator.configuration;

import org.no.generator.Provider;
import org.no.generator.Source;
import org.no.generator.Store;
import org.no.generator.configuration.context.DependencyContext;

public final class ProviderHandlerStore extends ConfigurationHandlerDefault<Provider, ProviderHandlerStore.Configuration> {

    @Override
    protected Provider create(Configuration c, DependencyContext context) {
        if (c.store == null) {
            throw new ConfigurationException("Field store should be specified");
        }
        Store store = context.get(Store.class, c.store);

        if (c.source == null) {
            throw new ConfigurationException("Field store should be specified");
        }
        Source source = context.get(Source.class, c.source);


        Object[] items = store.fetch();

        return () -> {
            return items[source.nextInt(items.length)];
        };
    }

    public static final class Configuration {

        private Object source;

        private Object store;

    }

}
