package org.no.generator.configuration.util;

import java.io.IOException;
import java.io.InputStream;

public class StreamUtils {

    public static long readLong(IncompleteByteContext context, InputStream is, int bits) {
        int pos = bits;

        long sum = 0;
        while (pos > 0) {

            // fill buffer
            if (context.bufferSize == 0) {
                context.buffer = readSilently(is);
                context.bufferSize = 8;
            }

            int size = context.bufferSize;
            if (pos < context.bufferSize) {
                size = pos;
            }

            sum += (context.buffer & (1L << size) - 1) << (bits - pos);
            pos -= size;

            // cleanup buffer
            context.buffer = context.buffer >> size;
            context.bufferSize = context.bufferSize - size;
        }

        return sum;
    }

    private static int readSilently(InputStream is) {
        try {
            return is.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static class IncompleteByteContext {

        private int bufferSize;

        private int buffer;

    }
}
