package org.no.generator;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Generators {

    private Generators() {}

    public static Generator createSystemTimeNanoseconds() {
        return a -> a.append(Long.toString(System.nanoTime()));
    }

    public static Generator createSystemTimeMicroseconds() {
        return a -> a.append(Long.toString(System.nanoTime() / 1000L));
    }

    public static Generator createSystemTimeMilliseconds() {
        return a -> a.append(Long.toString(System.nanoTime() / 1000000L));
    }

    public static Generator createSystemTimeSeconds() {
        return a -> a.append(Long.toString(System.nanoTime() / 1000000000L));
    }

    public static Generator createSystemTimeMinutes() {
        return a -> a.append(Long.toString(System.nanoTime() / 60000000000L));
    }

    public static Generator createSystemTimeHours() {
        return a -> a.append(Long.toString(System.nanoTime() / 3600000000000L));
    }

    public static Generator createSystemTimeDays() {
        return a -> a.append(Long.toString(System.nanoTime() / 86400000000000L));
    }

    public static Generator createCurrentDate() {
        return a -> a.append(new java.sql.Date(System.currentTimeMillis()).toString());
    }

    public static Generator createCurrentTime() {
        return a -> a.append(new java.sql.Time(System.currentTimeMillis()).toString());
    }

    public static Generator createCurrentDateTime() {
        return a -> a.append(new java.sql.Timestamp(System.currentTimeMillis()).toString());
    }

    public static Generator createNetworkIp() {
        return a -> {
            NetworkInterface networkInterface = getNonLoopbackNetworkInterface();

            if (networkInterface == null) {
                return;
            }

            InetAddress ia = null;
            Enumeration<InetAddress> ias = networkInterface.getInetAddresses();
            if (ias.hasMoreElements()) {
                ia = ias.nextElement();
            }

            if (ia == null) {
                return;
            }

            createString(ia.getHostAddress()).append(a);
        };
    }

    public static Generator createNetworkHost() {
        return a -> {
            NetworkInterface networkInterface = getNonLoopbackNetworkInterface();

            if (networkInterface == null) {
                return;
            }

            InetAddress ia = null;
            Enumeration<InetAddress> ias = networkInterface.getInetAddresses();
            if (ias.hasMoreElements()) {
                ia = ias.nextElement();
            }

            if (ia == null) {
                return;
            }

            createString(ia.getHostName()).append(a);
        };
    }

    public static Generator createNetworkMac() {
        return a -> {
            NetworkInterface networkInterface = getNonLoopbackNetworkInterface();

            if (networkInterface == null) {
                return;
            }

            byte[] ma;
            try {
                ma = networkInterface.getHardwareAddress();
            } catch (SocketException e) {
                throw new IllegalStateException();
            }

            if (ma == null) {
                ma = new byte[] { 0, 0, 0, 0, 0, 0 };
            }

            createStringHex(":", ma).append(a);;
        };
    }

    private static NetworkInterface getNonLoopbackNetworkInterface() {
        Enumeration<NetworkInterface> networkInterfaces;
        try {
            networkInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            throw new IllegalStateException();
        }

        NetworkInterface networkInterface = null;
        try {
            while (networkInterfaces.hasMoreElements()
                    && (networkInterface = networkInterfaces.nextElement()).isLoopback());
        } catch (SocketException e) {
            throw new IllegalStateException();
        }

        return networkInterface;
    }

    public static Generator createEmpty() {
        return a -> {};
    }

    public static Generator createUnion(Generator... generators) {
        return a -> {
            for (Generator g : generators) {
                g.append(a);
            }
         };
    }

    public static Generator createRound(Generator... generators) {
        AtomicInteger h = new AtomicInteger();
        return a -> {
            generators[h.getAndIncrement() % generators.length].append(a);
        };
    }

    public static Generator createAny(Generator[] generators, int total, int[] weights) {
        Source source = Sources.createDefault();
        return a -> {
            int i;
            while (true) {
                i = source.nextInt(generators.length);

                int w = weights[i];

                int v = source.nextInt(total);

                if (v < w) {
                    break;
                }
            }
            generators[i].append(a);
        };
    }

    public static Generator createAny(Source source, Generator... generators) {
        return a -> {
            generators[source.nextInt(generators.length)].append(a);
        };
    }

    public static Generator createString(String string) {
        return a -> a.append(string);
    }

    public static Generator createStringHex(String separator, byte... bytes) {
        char[] chars = "0123456789abcdef".toCharArray();

        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            if (separator != null && sb.length() > 0) {
                sb.append(separator);
            }
            sb.append(chars[b >> 4 & 15]);
            sb.append(chars[b >> 0 & 15]);
        }
        return a -> a.append(sb);
    }

    public static Generator createStringDec(String separator, byte... bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            if (separator != null && sb.length() > 0) {
                sb.append(separator);
            }
            sb.append(b);
        }
        return a -> a.append(sb);
    }

    public static String toString(Generator g) {
        StringBuilder sb = new StringBuilder();
        try {
            g.append(sb);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return sb.toString();
    }

    public static class WeightMarker {

        private final Generator value;

        private final int weight;

        public WeightMarker(Generator value, int weight) {
            super();
            this.value = value;
            this.weight = weight;
        }

        public Generator getValue() {
            return value;
        }

        public int getWeight() {
            return weight;
        }

    }
}
