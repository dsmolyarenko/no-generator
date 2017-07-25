package org.no.generator.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SplitUtils {

    private static Pattern pattern = Pattern.compile("(\\{(\\w+)(,(.*?))*?\\})");

    /**
     * Process text with placeholder expressions such as: Hello {name,c}.
     */
    public static void split(String string, HandlerText ht, HandlerName hn) {
        StringBuilder sb = new StringBuilder(string);

        while (sb.length() > 0) {
            Matcher m = pattern.matcher(sb);
            if (!m.find()) {
                break;
            }
            int index = m.start(1);
            if (index > 0) {
                ht.accept(sb.substring(0, index));
            }

            String name = m.group(2);
            if (name != null) {
                name = name.trim();
            }
            String function = m.group(4);
            if (function != null) {
                function = function.trim();
            }
            hn.accept(name, function);

            sb.delete(0, index + m.group(1).length());
        }

        if (sb.length() > 0) {
            ht.accept(sb.toString());
        }
    }

    public interface HandlerText {

        void accept(String text);

    }

    public interface HandlerName {

        void accept(String name, String function);

    }

}
