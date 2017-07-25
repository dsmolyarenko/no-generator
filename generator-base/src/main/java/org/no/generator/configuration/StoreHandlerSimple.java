package org.no.generator.configuration;

import static org.no.generator.configuration.util.ObjectUtils.apply;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;

import org.no.generator.Store;
import org.no.generator.configuration.context.DependencyContext;

public class StoreHandlerSimple extends ConfigurationHandlerDefault<Store, StoreHandlerSimple.Configuration> {

    @Override
    protected Store create(Configuration c, DependencyContext context) {

        Object[] data = new Object[c.data.length];

        Map<Integer, String> names = new LinkedHashMap<>();
        apply(names::put, c.names);

        Map<Integer, String> types = new LinkedHashMap<>();
        apply(types::put, c.types);

        if (names.size() > 0 && types.size() > 0 && names.size() != types.size()) {
            throw new ConfigurationException("Inconsistent number of elements in `names` and `types` sections: " + names.size() + ", " + types.size());
        }

        int n = 0;
        int l = names.size();
        for (Object object : c.data) {
            if (object == null) {
                throw new ConfigurationException("Unexpected null object in line: " + n);
            }
            Map<String, Object> r = new TreeMap<>();
            if (object instanceof List) {
                List<?> items = (List<?>) object;
                if (l != 0) {
                    if (l != items.size()) {
                        throw new ConfigurationException("Unexpected elements number in line: " + n);
                    }
                } else {
                    l = items.size();
                }
                int i = 0;
                for (Object item : items) {
                    String name = names.get(i);
                    if (name == null) {
                        name = Integer.toString(i);
                    }
                    r.put(name, item);
                    i++;
                }
            } else {
                if (object instanceof Map) {
                    Map<?, ?> items = (Map<?, ?>) object;
                    if (l != 0) {
                        if (l != items.size()) {
                            throw new ConfigurationException("Unexpected elements number in line: " + n);
                        }
                    } else {
                        l = items.size();
                    }
                    for (Object name : items.keySet()) {
                        if (names.size() != 0) {
                            if (!names.containsValue(name)) {
                                throw new ConfigurationException("Unexpected elements number in line: " + n);
                            }
                        }
                        r.put(name.toString().intern(), items.get(name));
                    }
                }
                r.put(null, object);
            }
            data[n++] = r;
        }

        int count = data.length;
        logger.log(Level.INFO, () -> getClass() + ": stored " + count + " item" + (count != 1 ? "s" : ""));

        return () -> {
            return data;
        };
    }

    public static final class Configuration {

        private String[] names;

        private String[] types;

        private Object[] data;

    }

}
