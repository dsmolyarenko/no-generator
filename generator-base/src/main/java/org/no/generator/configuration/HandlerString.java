package org.no.generator.configuration;

import org.no.generator.Writer;
import org.no.generator.configuration.context.DependencyContext;

public final class HandlerString extends ConfigurationHandlerDefault<Writer, HandlerString.Configuration> {

    @Override
    protected Writer create(Configuration c, DependencyContext context) {

        if (c.min >= c.max) {
            throw new IllegalArgumentException("Max character should go after min in the character table: " + c.min);
        }

        // create initial buffer
        char[] buffer = new char[c.length];
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = c.min;
        }

        return a -> {
            a.append(new String(buffer));

            //
            // roll characters
            //

            int position = 0;
            while (true) {
                int index = buffer.length - 1 - position; // inverse order
                char v = buffer[index];
                if (v < c.max) {
                    buffer[index] = ++v;
                    break;
                }
                buffer[index] = c.min;
                if (++position == buffer.length) {
                    position = 0;
                }
            }
        };
    }

    public static final class Configuration {

        private int length = 16;

        private char min = 'A';

        private char max = 'Z';

    }

//    Merge two types

//    protected Writer create(Configuration2 c, DependencyContext context) {
//
//        Source source = context.get(Source.class, c.source);
//
//        String string = "";
//        for (String range : c.ranges) {
//            if (range.charAt(1) == '-') {
//                for (char chr = range.charAt(0); chr <= range.charAt(2); chr++) {
//                    string += chr;
//                }
//            }
//        }
//
//        char[] characters = string.toCharArray();
//
//        return a -> {
//            StringBuilder sb = new StringBuilder();
//            for (int i = 0; i < c.length; i++) {
//                sb.append(characters[source.nextInt(characters.length)]);
//            }
//            a.append(sb);
//        };
//    }
//
//    public static final class Configuration2 {
//
//        private Object source;
//
//        private int length = 16;
//
//        private String[] ranges = { "0-9", "a-z", "A-Z" };
//
//    }
}
