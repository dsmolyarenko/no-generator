package org.no.generator;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Generators {

    private Generators() {}

    public static Writer createSystemTimeNanoseconds() {
        return a -> a.append(Long.toString(System.nanoTime()));
    }

    public static Writer createSystemTimeMicroseconds() {
        return a -> a.append(Long.toString(System.nanoTime() / 1000L));
    }

    public static Writer createSystemTimeMilliseconds() {
        return a -> a.append(Long.toString(System.nanoTime() / 1000000L));
    }

    public static Writer createSystemTimeSeconds() {
        return a -> a.append(Long.toString(System.nanoTime() / 1000000000L));
    }

    public static Writer createSystemTimeMinutes() {
        return a -> a.append(Long.toString(System.nanoTime() / 60000000000L));
    }

    public static Writer createSystemTimeHours() {
        return a -> a.append(Long.toString(System.nanoTime() / 3600000000000L));
    }

    public static Writer createSystemTimeDays() {
        return a -> a.append(Long.toString(System.nanoTime() / 86400000000000L));
    }

    public static Writer createCurrentDate() {
        return a -> a.append(new java.sql.Date(System.currentTimeMillis()).toString());
    }

    public static Writer createCurrentTime() {
        return a -> a.append(new java.sql.Time(System.currentTimeMillis()).toString());
    }

    public static Writer createCurrentDateTime() {
        return a -> a.append(new java.sql.Timestamp(System.currentTimeMillis()).toString());
    }

    public static Writer createNetworkIp() {
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

    public static Writer createNetworkHost() {
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

    public static Writer createNetworkMac() {
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

    public static Writer createEmpty() {
        return a -> {};
    }

    public static Writer createUnion(Writer... generators) {
        return a -> {
            for (Writer g : generators) {
                g.append(a);
            }
         };
    }

    public static Writer createRound(Writer... generators) {
        AtomicInteger h = new AtomicInteger();
        return a -> {
            generators[h.getAndIncrement() % generators.length].append(a);
        };
    }

    public static Writer createString(String string) {
        return a -> a.append(string);
    }

    public static Writer createStringHex(String separator, byte... bytes) {
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

    public static Writer createStringDec(String separator, byte... bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            if (separator != null && sb.length() > 0) {
                sb.append(separator);
            }
            sb.append(b);
        }
        return a -> a.append(sb);
    }

    public static String toString(Writer g) {
        StringBuilder sb = new StringBuilder();
        try {
            g.append(sb);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return sb.toString();
    }

    public static class WeightMarker {

        private final Writer value;

        private final int weight;

        public WeightMarker(Writer value, int weight) {
            super();
            this.value = value;
            this.weight = weight;
        }

        public Writer getValue() {
            return value;
        }

        public int getWeight() {
            return weight;
        }

    }
}
