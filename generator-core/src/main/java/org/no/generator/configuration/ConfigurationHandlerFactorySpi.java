package org.no.generator.configuration;

import java.util.Iterator;
import java.util.ServiceLoader;

public class ConfigurationHandlerFactorySpi implements ConfigurationHandlerFactory {

    private final ServiceLoader<ConfigurationHandlerFactory> serviceLoader;

    private ConfigurationHandlerFactorySpi() {
        this.serviceLoader = ServiceLoader.load(ConfigurationHandlerFactory.class);
    }

    @Override
    public <T> ConfigurationHandler<T> get(Class<T> cc, String ct) {
        Iterator<ConfigurationHandlerFactory> configurationHandlerFactories = serviceLoader.iterator();
        while (configurationHandlerFactories.hasNext()) {
            ConfigurationHandler<T> configurationHandlerFactory = configurationHandlerFactories.next().get(cc, ct);
            if (configurationHandlerFactory != null) {
                return configurationHandlerFactory;
            }
        }
        return null;
    }

    private static ConfigurationHandlerFactory service;

    public static synchronized ConfigurationHandlerFactory getInstance() {
        if (service == null) {
            service = new ConfigurationHandlerFactorySpi();
        }
        return service;
    }

}