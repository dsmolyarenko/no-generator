package org.no.generator.configuration;

import static org.no.generator.configuration.util.ObjectUtils.map;

import org.no.generator.Generator;
import org.no.generator.Source;
import org.no.generator.configuration.context.DependencyContext;

public final class HandlerRandomAny extends ConfigurationHandlerDefault<Generator, HandlerRandomAny.Configuration> {

    @Override
    protected Generator create(Configuration c, DependencyContext context) {

        Source source = context.get(Source.class, c.source);

        Generator[] samples = map(c.samples, t -> context.get(Generator.class, t), Generator.class);

        return a -> samples[source.nextInt(samples.length)].append(a);
    }

    static final class Configuration {

        Object source;

        Object[] samples;

    }

}
