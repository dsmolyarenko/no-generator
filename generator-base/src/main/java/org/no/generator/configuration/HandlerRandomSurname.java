package org.no.generator.configuration;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.no.generator.Generator;
import org.no.generator.Source;
import org.no.generator.configuration.context.DependencyContext;

public final class HandlerRandomSurname extends ConfigurationHandlerDefault<Generator, HandlerRandomSurname.Configuration> {

    @Override
    protected Generator create(Configuration c, DependencyContext context) {

        String[] candidates = getCandidates(c.lang);

        Source source = context.get(Source.class, c.source);

        return a -> a.append(candidates[source.nextInt(candidates.length)]);
    }

    private static String[] getCandidates(String lang) {
        List<String> result = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(HandlerRandomSurname.class.getResource("/db-surnames.csv").toURI()))) {
            stream.forEach(l -> {
                String[] row = l.split(",");
                if (lang != null) {
                    if (!row[1].contains(lang)) {
                        return;
                    }
                }
                result.add(row[0]);
            });
        } catch (IOException | URISyntaxException e) {
            throw new IllegalStateException();
        }
        return result.toArray(new String[result.size()]);
    }

    public static final class Configuration {

        private Object source;

        private String lang;

    }

}
