package org.no.generator.configuration.context;

public interface DependencyContext {

    default <T> T get(Class<T> type, Object o) {
        return get(type, o, this);
    }

    <T> T get(Class<T> type, Object o, DependencyContext context);
}
