package org.no.generator.configuration;

import java.util.function.Supplier;

import org.apache.commons.math3.random.AbstractWell;
import org.apache.commons.math3.random.Well1024a;
import org.apache.commons.math3.random.Well19937a;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.random.Well44497a;
import org.apache.commons.math3.random.Well44497b;
import org.no.generator.Sequence;
import org.no.generator.configuration.context.DependencyContext;

public class SequenceHandlerWell extends ConfigurationHandlerDefault<Sequence, SequenceHandlerWell.Configuration> {

    @Override
    protected Sequence create(Configuration c, DependencyContext context) {
        AbstractWell well = c.pool.getWell();
        return (min, max, bounded, weights) -> {
            if (max < min) {
                throw new IllegalArgumentException("Min value should be less or equals to max");
            }
            return () -> min + well.nextInt(min - max);
        };
    }

    public static final class Configuration {

        private Pool pool = Pool.Well1024a;

        public enum Pool {

            Well1024a (() -> new Well1024a() ),
            Well19937a(() -> new Well19937a()),
            Well19937c(() -> new Well19937c()),
            Well44497a(() -> new Well44497a()),
            Well44497b(() -> new Well44497b()),
            Well512a  (() -> new Well1024a() ),
            ;

            private Supplier<AbstractWell> supplier;

            private Pool(Supplier<AbstractWell> supplier) {
                this.supplier = supplier;
            }

            public AbstractWell getWell() {
                return supplier.get();
            }
        }
    }

}
