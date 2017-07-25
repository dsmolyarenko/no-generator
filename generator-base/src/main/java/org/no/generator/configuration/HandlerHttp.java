package org.no.generator.configuration;

import java.io.BufferedReader;
import java.io.StringReader;

import org.no.generator.Generator;
import org.no.generator.GeneratorValueHolder;
import org.no.generator.configuration.context.DependencyContext;

public final class HandlerHttp extends ConfigurationHandlerDefault<Generator, HandlerHttp.Configuration> {

    private static final String CRLF = "\r\n";

    @Override
    protected Generator create(Configuration c, DependencyContext context) {

        if (c.head == null) {
            throw new ConfigurationException("Property `head` is mandatory");
        }

        Generator head = context.get(Generator.class, c.head);

        Generator body = c.body != null
            ? context.get(Generator.class, c.body)
            : null;

        return a -> {
            try (BufferedReader br = new BufferedReader(new StringReader(new GeneratorValueHolder(head).getValue()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.length() > 0) {
                        a.append(line);
                        a.append(CRLF);
                    }
                }
            }
            a.append("Connection: keep-alive");
            a.append(CRLF);

            String contentBody = null;
            if (body != null) {
                contentBody = new GeneratorValueHolder(body).getValue();

                a.append("Content-Length: ");
                a.append(Integer.toString(contentBody.length()));
                a.append(CRLF);
            }
            a.append(CRLF);

            if (contentBody != null) {
                a.append(contentBody);
            }
        };
    }

    public static final class Configuration {

        private Object head;

        private Object body;

    }

}
