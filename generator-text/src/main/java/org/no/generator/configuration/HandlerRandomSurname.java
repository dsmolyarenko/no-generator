package org.no.generator.configuration;

import org.no.generator.Generator;
import org.no.generator.Source;
import org.no.generator.configuration.context.DependencyContext;
import org.no.generator.configuration.util.CSVUtils;
import org.no.generator.configuration.util.CSVUtils.SearchCriteria;

public final class HandlerRandomSurname extends ConfigurationHandlerDefault<Generator, HandlerRandomSurname.Configuration> {

    @Override
    protected Generator create(Configuration c, DependencyContext context) {

        Source source = context.get(Source.class, c.source);

        SearchCriteria searchCriteria = new SearchCriteria("Name");
        if (c.l != null) {
            searchCriteria.matches("Origin", c.l);
        }

        String[] candidates = CSVUtils.findBy("/db-surnames.csv", searchCriteria);
        if (candidates.length == 0) {
            throw new ConfigurationException("No elements matching criteria: " + searchCriteria);
        }

        return a -> a.append(candidates[source.nextInt(candidates.length)]);
    }

    public static final class Configuration {

        private Object source;

        private String l;

    }

}
