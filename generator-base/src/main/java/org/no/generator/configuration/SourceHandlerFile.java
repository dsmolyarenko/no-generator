package org.no.generator.configuration;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.no.generator.Source;
import org.no.generator.configuration.context.DependencyContext;
import org.no.generator.configuration.util.SourceUtils;
import org.no.generator.configuration.util.StreamUtils;
import org.no.generator.configuration.util.StreamUtils.IncompleteByteContext;

public class SourceHandlerFile extends ConfigurationHandlerDefault<Source, SourceHandlerFile.Configuration> {

    @Override
    protected Source create(Configuration c, DependencyContext context) {
        InputStream dis;
        try {
            dis = new DataInputStream(c.bs > 0
                ? new BufferedInputStream(new FileInputStream(c.path))
                : new FileInputStream(c.path));
        } catch (FileNotFoundException e) {
            throw new ConfigurationException("Unable to find file: " + c.path);
        }

        double max = 1L << c.bl;

        IncompleteByteContext ctx = new IncompleteByteContext();

        return SourceUtils.decorate(() -> StreamUtils.readLong(ctx, dis, c.bl) / max, c.distribution);
    }

    public static final class Configuration {

        private int bl = 16;

        private int bs = 8192;

        private String path;

        private double[] distribution;
    }

}
