package org.no.generator.configuration;

import static org.no.generator.configuration.util.ObjectUtils.map;
import static org.no.generator.configuration.util.ObjectUtils.parameter;

import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.no.generator.configuration.context.DependencyContext;

public abstract class ConfigurationHandlerDefault<E, C> implements ConfigurationHandler<E> {

    protected Logger logger = Logger.getGlobal();

    @Override
    public E handle(Map<String, Object> configuration, DependencyContext context) {
        return create(map(configuration, parameter(getClass(), 1)), context);
    }

    protected abstract E create(C c, DependencyContext context);

    protected void info(Supplier<String> info) {
        logger.log(Level.INFO, info);
    }

    protected void warn(Supplier<String> warning) {
        logger.log(Level.WARNING, warning);
    }

}
