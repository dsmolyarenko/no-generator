package org.no.generator.configuration;

public interface ConfigurationHandlerFactory {

    <T> ConfigurationHandler<T> get(Class<T> cc, String ct);

}