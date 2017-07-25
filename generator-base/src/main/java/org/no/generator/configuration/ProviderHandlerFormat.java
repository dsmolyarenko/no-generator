package org.no.generator.configuration;

import static org.no.generator.configuration.util.ObjectUtils.get;
import static org.no.generator.util.SplitUtils.split;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import org.no.generator.Provider;
import org.no.generator.configuration.context.DependencyContext;

public final class ProviderHandlerFormat extends ConfigurationHandlerDefault<Provider, ProviderHandlerFormat.Configuration> {

    @Override
    protected Provider create(Configuration c, DependencyContext context) {
        if (c.provider == null) {
            throw new ConfigurationException("Field provider should be specified");
        }

        Provider provider = context.get(Provider.class, c.provider);

        List<BiConsumer<StringBuilder, Object>> appenders = new ArrayList<>();
        split(c.format, (t) -> appenders.add((s, o) -> s.append(t)), (n, a) -> {
            if (n.equals("this")) {
                appenders.add((s, o) -> s.append(o));
            } else {
                appenders.add((s, o) -> s.append(get(o, n)));
            }
        });

        return () -> {
            Object o = provider.next();

            StringBuilder s = new StringBuilder();
            for (BiConsumer<StringBuilder, Object> a : appenders) {
                a.accept(s, o);
            }
            return s;
        };
    }

    public static final class Configuration {

        private Object provider;

        private String format;

    }

}
