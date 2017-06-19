package org.no.generator.configuration.context;

public interface DependencyContext {

    <T> T get(Class<T> type, Object o);

}
