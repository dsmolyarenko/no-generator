package org.no.generator.configuration.util;

import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static void split(Pattern pattern, String string, Consumer<String> space, Consumer<String> group) {
        StringBuilder sb = new StringBuilder(string);

        while (true) {
            Matcher m = pattern.matcher(sb);
            if (!m.find()) {
                break;
            }

            int index = m.start(1);
            if (index > 0) {
                space.accept(sb.substring(0, index));
            }

            group.accept(m.group(2));

            sb.delete(0, index + m.group(1).length());
        }

        if (sb.length() > 0) {
            space.accept(sb.toString());
        }
    }
}
