package org.no.generator.configuration;

import org.no.generator.Generator;
import org.no.generator.Source;
import org.no.generator.configuration.context.DependencyContext;

public final class HandlerRandomString extends ConfigurationHandlerDefault<Generator, HandlerRandomString.Configuration> {

    @Override
    protected Generator create(Configuration c, DependencyContext context) {

        Source source = context.get(Source.class, c.source);

        String string = "";
        for (String range : c.ranges) {
            if (range.charAt(1) == '-') {
                for (char chr = range.charAt(0); chr <= range.charAt(2); chr++) {
                    string += chr;
                }
            }
        }

        char[] characters = string.toCharArray();

        return a -> {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < c.length; i++) {
                sb.append(characters[source.nextInt(characters.length)]);
            }
            a.append(sb);
        };
    }

    public static final class Configuration {

        private Object source;

        private int length = 16;

        private String[] ranges;

    }

}
