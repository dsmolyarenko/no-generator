package org.no.generator.configuration;

import org.apache.commons.lang3.mutable.MutableInt;
import org.no.generator.DataSourceExhaustedException;
import org.no.generator.Sequence;
import org.no.generator.configuration.context.DependencyContext;

public class SequenceHandlerSequential extends ConfigurationHandlerDefault<Sequence, SequenceHandlerSequential.Configuration> {

    @Override
    protected Sequence create(Configuration c, DependencyContext context) {
        return (min, max, bounded, weights) -> {
            MutableInt mutableInt = new MutableInt();
            mutableInt.setValue(min);
            return () -> {
                int index = mutableInt.getAndIncrement();
                if (index == max) {
                    if (bounded) {
                        throw new DataSourceExhaustedException();
                    }
                    index = min;
                }
                return index;
            };
        };

    }

    public static final class Configuration {

    }

}

