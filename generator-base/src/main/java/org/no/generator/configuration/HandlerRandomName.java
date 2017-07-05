package org.no.generator.configuration;

import org.no.generator.Generator;
import org.no.generator.Source;
import org.no.generator.configuration.context.DependencyContext;
import org.no.generator.configuration.util.TextUtils;

public final class HandlerRandomName extends ConfigurationHandlerDefault<Generator, HandlerRandomName.Configuration> {

    @Override
    protected Generator create(Configuration c, DependencyContext context) {

        String[] candidates = TextUtils.getNames(TextUtils.getNameCriteria(c.m, c.f, c.lang));

        Source source = context.get(Source.class, c.source);

        return a -> a.append(candidates[source.nextInt(candidates.length)]);
    }

    public static final class Configuration {

        private Object source;

        private String lang;

        private String m;

        private String f;

    }

}
