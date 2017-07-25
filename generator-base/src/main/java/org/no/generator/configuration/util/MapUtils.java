package org.no.generator.configuration.util;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class MapUtils {

    public static class MapBuilder<K, V> {

        private final Map<K, V> map;

        public MapBuilder() {
            this.map = new LinkedHashMap<>();
        }

        public MapBuilder<K, V> put(K k, V v) {
            map.put(k, v);
            return this;
        }

        public Map<K, V> build() {
            return Collections.unmodifiableMap(map);
        }
    }

}
