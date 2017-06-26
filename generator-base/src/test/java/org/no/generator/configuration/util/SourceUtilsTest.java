package org.no.generator.configuration.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;
import org.no.generator.configuration.util.StreamUtils.IncompleteByteContext;

public class SourceUtilsTest {

    @Test
    public void readTestA() {

        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[] {
                (byte) 0b11000111,
                (byte) 0b01110001,
                (byte) 0b00011100,
        });

        IncompleteByteContext ctx = new IncompleteByteContext();

        Assert.assertEquals(7L, StreamUtils.readLong(ctx, bais, 3));
        Assert.assertEquals(0L, StreamUtils.readLong(ctx, bais, 3));
        Assert.assertEquals(7L, StreamUtils.readLong(ctx, bais, 3));
        Assert.assertEquals(0L, StreamUtils.readLong(ctx, bais, 3));
        Assert.assertEquals(7L, StreamUtils.readLong(ctx, bais, 3));
        Assert.assertEquals(0L, StreamUtils.readLong(ctx, bais, 3));
        Assert.assertEquals(7L, StreamUtils.readLong(ctx, bais, 3));
        Assert.assertEquals(0L, StreamUtils.readLong(ctx, bais, 3));
    }

    @Test
    public void readTestB() {

        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[] {
                (byte) 0b11000111,
                (byte) 0b11000111,
                (byte) 0b11000111,
        });

        IncompleteByteContext ctx = new IncompleteByteContext();
        Assert.assertEquals(199L, StreamUtils.readLong(ctx, bais, 8));
        Assert.assertEquals(199L, StreamUtils.readLong(ctx, bais, 8));
        Assert.assertEquals(199L, StreamUtils.readLong(ctx, bais, 8));
    }

    @Test
    public void readTestC() {

        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[] {
                (byte) 0b00000100,
                (byte) 0b00001001,
                (byte) 0b00000010,
        });

        IncompleteByteContext ctx = new IncompleteByteContext();
        Assert.assertEquals(260L, StreamUtils.readLong(ctx, bais, 9));
        Assert.assertEquals(260L, StreamUtils.readLong(ctx, bais, 9));
    }

    @Test
    public void readTestC1() throws IOException {

        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[] {
                (byte) 0b11000111,
                (byte) 0b01110001,
                (byte) 0b00011100,
        });

        BitStream bs = new BitStream(bais);

        Assert.assertEquals(7L, bs.nextBits(3));
        Assert.assertEquals(0L, bs.nextBits(3));
        Assert.assertEquals(7L, bs.nextBits(3));
        Assert.assertEquals(0L, bs.nextBits(3));
        Assert.assertEquals(7L, bs.nextBits(3));
        Assert.assertEquals(0L, bs.nextBits(3));
        Assert.assertEquals(7L, bs.nextBits(3));
        Assert.assertEquals(0L, bs.nextBits(3));
    }

    static class BitStream {

        private final InputStream in;

        /** The bits read from the underlying stream but not consumed by nextBits() */
        private long bitCache;

        /** The number of bits available in the bit cache */
        private int bitCacheSize;

        /** Bit masks for extracting the right most bits from a byte */
        private static final int[] MASKS = new int[]{
                0x00, // 00000000
                0x01, // 00000001
                0x03, // 00000011
                0x07, // 00000111
                0x0F, // 00001111
                0x1F, // 00011111
                0x3F, // 00111111
                0x7F, // 01111111
                0xFF  // 11111111
        };

        BitStream(InputStream in) {
            this.in = in;
        }

        private boolean fillCache() throws IOException {
            boolean filled = false;

            while (bitCacheSize <= 56) {
                long nextByte = in.read();
                if (nextByte == -1) {
                    break;
                }

                filled = true;
                bitCache = bitCache | (nextByte << bitCacheSize);
                bitCacheSize += 8;
            }

            return filled;
        }

        /**
         * Returns the next bit.
         *
         * @return The next bit (0 or 1) or -1 if the end of the stream has been reached
         */
        int nextBit() throws IOException {
            if (bitCacheSize == 0 && !fillCache()) {
                return -1;
            }

            int bit = (int) (bitCache & 1); // extract the right most bit

            bitCache = (bitCache >>> 1); // shift the remaning bits to the right
            bitCacheSize--;

            return bit;
        }

        /**
         * Returns the integer value formed by the n next bits (up to 8 bits).
         *
         * @param n the number of bits read (up to 8)
         * @return The value formed by the n bits, or -1 if the end of the stream has been reached
         */
        int nextBits(final int n) throws IOException {
            if (bitCacheSize < n && !fillCache()) {
                return -1;
            }

            final int bits = (int) (bitCache & MASKS[n]); // extract the right most bits

            bitCache = (bitCache >>> n); // shift the remaning bits to the right
            bitCacheSize = bitCacheSize - n;

            return bits;
        }

        int nextByte() throws IOException {
            return nextBits(8);
        }
    }
}
