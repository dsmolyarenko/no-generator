package org.no.generator.configuration;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.no.generator.Sequence;
import org.no.generator.configuration.context.DependencyContext;
import org.no.generator.configuration.util.StreamUtils;
import org.no.generator.configuration.util.StreamUtils.IncompleteByteContext;

public class SequenceHandlerFile extends ConfigurationHandlerDefault<Sequence, SequenceHandlerFile.Configuration> {

    @Override
    protected Sequence create(Configuration c, DependencyContext context) {
        InputStream dis;
        try {
            dis = new DataInputStream(c.bs > 0
                ? new BufferedInputStream(new FileInputStream(c.path))
                : new FileInputStream(c.path));
        } catch (FileNotFoundException e) {
            throw new ConfigurationException("Unable to find file: " + c.path);
        }

        IncompleteByteContext ctx = new IncompleteByteContext();

        return (min, max, bounded, weights) -> {
            if (max < min) {
                throw new IllegalArgumentException("Min value should be less or equals than max");
            }
            int length = max - min;
            return () ->  {
                return min + (int) StreamUtils.readLong(ctx, dis, c.bc) % length;
            };
        };
    }

    public static final class Configuration {

        /**
         * Bit count.
         */
        private int bc = 16;

        /**
         * Buffer size.
         */
        private int bs = 128;

        /**
         * source file path.
         */
        private String path;

    }

    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
    }
}
