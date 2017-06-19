package org.no.generator.configuration;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import org.no.generator.configuration.util.ObjectUtils;

public class ConfigurationHandlerFactoryDefault implements ConfigurationHandlerFactory {

    private Map<Class<?>, Map<String, ConfigurationHandler<?>>> handlers;

    public <T> ConfigurationHandlerFactoryDefault add(Class<T> cc, String ct, ConfigurationHandler<T> c) {
        if (handlers == null) {
            handlers = new LinkedHashMap<>();
        }
        Map<String, ConfigurationHandler<?>> map = handlers.get(cc);
        if (map == null) {
            handlers.put(cc, map = new TreeMap<>(String.CASE_INSENSITIVE_ORDER));
        }
        map.put(ct, c);
        return this;
    }

    @Override
    public <T> ConfigurationHandler<T> get(Class<T> cc, String ct) {
        Map<String, ConfigurationHandler<T>> map = ObjectUtils.safe(handlers.get(cc));
        if (map == null) {
            return null;
        }
        return map.get(ct);
    }

}