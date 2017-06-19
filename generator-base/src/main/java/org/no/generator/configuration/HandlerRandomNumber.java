package org.no.generator.configuration;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.no.generator.Generator;
import org.no.generator.Source;
import org.no.generator.configuration.context.DependencyContext;

public final class HandlerRandomNumber extends ConfigurationHandlerDefault<Generator, HandlerRandomNumber.Configuration> {

    @Override
    protected Generator create(Configuration c, DependencyContext context) {

        Source source = context.get(Source.class, c.source);

        DecimalFormat f;
        if (c.format != null) {
            if (c.locale != null) {
                f = new DecimalFormat(c.format, DecimalFormatSymbols.getInstance(c.locale));
            } else {
                f = new DecimalFormat(c.format);
            }
        } else {
            f = new DecimalFormat();
        }

        return a -> a.append(f.format(c.shift + c.scale * source.nextDouble()));
    }

    public static final class Configuration {

        private Object source;

        private String format;

        private Locale locale;

        private double shift;

        private double scale;

    }

}
