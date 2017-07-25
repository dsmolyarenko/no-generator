package org.no.generator.configuration.util;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectUtils {

    @SuppressWarnings("unchecked")
    public static <T> T safe(Object o) {
        return (T) o;
    }

    public static Object get(Object object, String field) {
        return getObjectMapper().convertValue(object, Map.class).get(field);
    }

    public static void each(Object object, BiConsumer<String, Object> action) {
        Map<String, Object> map = getObjectMapper().convertValue(object, new TypeReference<Map<String, Object>>() {});
        map.forEach(action);
    }

    public static <T, V> V[] map(T[] objects, Function<T, V> transformer, Class<V> type) {

        if (objects == null) {
            return null;
        }

        V[] result = safe(Array.newInstance(type, objects.length));

        for (int i = 0; i < objects.length; i++) {
            result[i] = transformer.apply(objects[i]);
        }

        return result;
    }

    public static <E, T, C extends Throwable> Object[] apply(Transformer<E, Object, C> f, E... objects) throws C {

        if (objects == null || objects.length == 0) {
            return null;
        }

        if (objects.length == 1) {
            return new Object[] { f.apply(objects[0]) };
        }

        Object[] result = new Object[objects.length];
        for (int i = 0; i < objects.length; i++) {
            result[i] = f.apply(objects[i]);
        }
        return result;
    }

    public static <V, E extends Throwable> void apply(Applier<Integer, V, E> a, V... objects) throws E {
        if (objects == null) {
            return;
        }
        for (int i = 0; i < objects.length; i++) {
            a.apply(i, objects[i]);
        }
    }

    public static <K, V, E extends Throwable> void apply(Applier<K, V, E> a, Map<K, V> map) throws E {
        if (map == null) {
            return;
        }
        for (K k : map.keySet()) {
            V v = map.get(k);
            if (v != null) {
                a.apply(k, v);
            }
        }
    }

    public static interface Transformer<T, R, E extends Throwable> {

        R apply(T t) throws E;

    }

    public static interface Applier<I, V, E extends Throwable> {

        void apply(I i, V v) throws E;

    }

    public static <T> Object[] map(Function<T, Object> f, T... objects) {

        if (objects == null || objects.length == 0) {
            return null;
        }

        if (objects.length == 1) {
            return new Object[] { f.apply(objects[0]) };
        }

        Object[] result = new Object[objects.length];
        for (int i = 0; i < objects.length; i++) {
            result[i] = f.apply(objects[i]);
        }
        return result;
    }


    public static <T> T map(Object o, Class<T> t) {
        return getObjectMapper().convertValue(o, t);
    }

    public static Map<String, Object> merge(Map<String, Object> src, Map<String, Object> updates) {
        TypeReference<Map<String, Object>> type = new TypeReference<Map<String, Object>>() {};

        Map<String, Object> srcCopy;
        try {
            srcCopy = getObjectMapper().readValue(getObjectMapper().writeValueAsString(src), type);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        update(srcCopy, updates);

        return safe(srcCopy);
    }

    public static void update(Map<String, Object> src, Map<String, Object> updates) {
        for (String key : updates.keySet()) {
            Object update = updates.get(key);
            if (src.containsKey(key)) {
                Object original = src.get(key);
                if (original instanceof Map && update instanceof Map) {
                    update(ObjectUtils.<Map<String, Object>>safe(original), ObjectUtils.<Map<String, Object>>safe(update));
                } else {
                    src.put(key, update);
                }
            } else {
                src.put(key, update);
            }
        }
    }

    public static <T> Class<T> parameter(Class<?> type, int i) {
        Type t = type.getGenericSuperclass();
        if (t == Object.class) {
            Type[] interfaces = type.getGenericInterfaces();
            if (interfaces.length > 0) {
                t = interfaces[0];
            }
        }
        if (t instanceof ParameterizedType) {
            ParameterizedType p = (ParameterizedType) t;
            if (i < p.getActualTypeArguments().length) {
                return safe(p.getActualTypeArguments()[i]);
            }
        }
        throw new IllegalArgumentException("Type isn't parametrized: " + t);
    }

    public static <T> boolean in(T object, T... objects) {
        for (T o : objects) {
            if (object.equals(o)) {
                return true;
            }
        }
        return false;
    }

    private static ObjectMapper objectMapper;

    private static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            synchronized (ObjectUtils.class) {
                if (objectMapper == null) {
                    objectMapper = createObjectMapper();
                }
            }

        }
        return objectMapper;
    }

    private static ObjectMapper createObjectMapper() {
        return new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
    }
}
