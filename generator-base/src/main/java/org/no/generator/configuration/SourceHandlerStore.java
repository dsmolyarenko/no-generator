package org.no.generator.configuration;

import java.util.function.Supplier;

import org.apache.commons.lang3.math.NumberUtils;
import org.no.generator.Sequence;
import org.no.generator.Source;
import org.no.generator.Store;
import org.no.generator.configuration.context.DependencyContext;
import org.no.generator.configuration.util.ObjectUtils;

public final class SourceHandlerStore extends ConfigurationHandlerDefault<Source, SourceHandlerStore.Configuration> {

    @Override
    protected Source create(Configuration c, DependencyContext context) {
        if (c.store == null) {
            throw new ConfigurationException("Field `store` should be specified");
        }
        Store store = context.get(Store.class, c.store);

        if (c.sequence == null) {
            throw new ConfigurationException("Field `sequence` should be specified");
        }
        Sequence sequence = context.get(Sequence.class, c.sequence);

        double[] weights = null;
        if (c.storeWeightField != null) {
            int size = store.size();

            weights = new double[size];
            for (int i = 0; i < size; i++) {
                Object weight = ObjectUtils.get(store.data(i), c.storeWeightField);
                if (weight == null) {
                    throw new ConfigurationException("Weight field " + c.storeWeightField + " of store " + c.store + " should not be null or empty");
                }
                if (weight instanceof Number) {
                    weights[i] = Number.class.cast(weight).doubleValue();
                } else {
                    weights[i] = NumberUtils.toDouble(weight.toString());
                }
            };
        }

        Supplier<Integer> supplier = sequence.create(0, store.size(), false, weights);
        return () -> {
            return store.data(supplier.get());
        };
    }

    public static final class Configuration {

        private Object sequence;

        private Object store;

        private String storeWeightField;

    }

}
