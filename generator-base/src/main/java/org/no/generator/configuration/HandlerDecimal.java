package org.no.generator.configuration;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.no.generator.Writer;
import org.no.generator.configuration.context.DependencyContext;
import org.no.generator.util.MutableNum;

public final class HandlerDecimal extends ConfigurationHandlerDefault<Writer, HandlerDecimal.Configuration> {

    @Override
    protected Writer create(Configuration c, DependencyContext context) {
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

        MutableNum mutableNum = new MutableNum(c.next);
        mutableNum.setMin(c.min);
        mutableNum.setMax(c.max);
        return a -> {
            a.append(f.format(mutableNum.getAndAdd(c.step)));
        };
    }

    public static final class Configuration {

        private String format;

        private Locale locale;

        private double min;

        private double max;

        private double step = 1;

        private double next;

    }
}
