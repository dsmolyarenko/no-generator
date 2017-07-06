package org.no.generator.configuration;

import org.no.generator.Generator;
import org.no.generator.Source;
import org.no.generator.configuration.context.DependencyContext;
import org.no.generator.configuration.util.CSVUtils;
import org.no.generator.configuration.util.CSVUtils.SearchCriteria;

public final class HandlerRandomName extends ConfigurationHandlerDefault<Generator, HandlerRandomName.Configuration> {

    @Override
    protected Generator create(Configuration c, DependencyContext context) {

        Source source = context.get(Source.class, c.source);

        SearchCriteria searchCriteria = new SearchCriteria("Name");
        if (c.l != null) {
            searchCriteria.matches("Origin", c.l);
        }
        if (c.g != null) {
            searchCriteria.matches("Gender", c.g);
        }

        String[] candidates = CSVUtils.findBy("/db-names.csv", searchCriteria);
        if (candidates.length == 0) {
            throw new ConfigurationException("No elements matching criteria: " + searchCriteria);
        }

        return a -> a.append(candidates[source.nextInt(candidates.length)]);
    }

    public static final class Configuration {

        private Object source;

        private Object l;

        private Object g;

    }

}
