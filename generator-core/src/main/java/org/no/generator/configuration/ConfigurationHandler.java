package org.no.generator.configuration;

import java.util.Map;

import org.no.generator.configuration.context.DependencyContext;

public interface ConfigurationHandler<E> {

    E handle(Map<String, Object> configuration, DependencyContext context);

}
