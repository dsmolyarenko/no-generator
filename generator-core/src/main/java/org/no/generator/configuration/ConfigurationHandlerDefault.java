package org.no.generator.configuration;

import static org.no.generator.configuration.util.ObjectUtils.map;
import static org.no.generator.configuration.util.ObjectUtils.parameter;

import java.util.Map;

import org.no.generator.configuration.context.DependencyContext;

public abstract class ConfigurationHandlerDefault<E, C> implements ConfigurationHandler<E> {

    @Override
    public E handle(Map<String, Object> configuration, DependencyContext context) {
        return create(map(configuration, parameter(getClass(), 1)), context);
    }

    protected abstract E create(C c, DependencyContext context);

}
