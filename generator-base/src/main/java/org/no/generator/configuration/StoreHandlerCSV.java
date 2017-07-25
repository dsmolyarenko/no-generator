package org.no.generator.configuration;

import static org.no.generator.configuration.util.ObjectUtils.apply;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import org.no.generator.Store;
import org.no.generator.configuration.context.DependencyContext;

public class StoreHandlerCSV extends ConfigurationHandlerDefault<Store, StoreHandlerCSV.Configuration> {

    @Override
    protected Store create(Configuration c, DependencyContext context) {

        if (c.path == null && c.data == null || c.path != null && c.data != null) {
            throw new ConfigurationException("Either `path` or `data` should be specified");
        }

        BufferedReader reader;
        if (c.path != null) {
            try {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(c.path), c.charset));
            } catch (FileNotFoundException e) {
                throw new ConfigurationException("Unable to open specified fiel: " + c.path);
            }
        } else {
            reader = new BufferedReader(new StringReader(c.data));
        }

        // read line with column names
        String[] head = read(reader);
        if (head == null) {
            throw new ConfigurationException("Input file or data is empty");
        }

        Map<Integer, String> names = new HashMap<>();
        Map<Integer, String> types = new HashMap<>();
        apply((i, v) -> {
            names.put(i, extractName(v));
            types.put(i, extractType(v));
        }, head);

        // read lines with data
        List<Object> objects = new ArrayList<>();
        for (String[] line; (line = read(reader)) != null;) {
            objects.add(createObject(line, names, types));
        }

        try {
            reader.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        Object[] data = objects.toArray();

        info(() -> getClass() + ": stored " + data.length + " item" + (data.length != 1 ? "s" : ""));

        return () -> {
            return data;
        };
    }

    private static Object createObject(String[] source, Map<Integer, String> names, Map<Integer, String> types) {
        Map<String, Object> result = new TreeMap<>();
        apply((i, v) -> {
            result.put(names.get(i), convertType(v, types.get(i)));
        }, source);
        return result;
    }

    private static String extractName(String v) {
        int index = v.indexOf(":");
        if (index == -1) {
            return v.toLowerCase().intern();
        }
        return v.substring(0, index).toLowerCase().intern();
    }

    private static String extractType(String v) {
        int index = v.indexOf(":");
        if (index == -1) {
            return null;
        }
        return v.substring(index + 1);
    }

    private static Object convertType(String v, String type) {
        if (type == null) {
            return v;
        }
        throw new UnsupportedOperationException("Type support is not implemented yet");
    }

    private static Pattern pattern = Pattern.compile(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

    private static String[] read(BufferedReader reader) {
        String line;
        try {
            line = reader.readLine();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        if (line == null) {
            return null;
        }
        return pattern.split(line);
    }

    public static final class Configuration {

        private Charset charset = Charset.defaultCharset();

        private String path;

        private String data;

        public Configuration withPath(String path) {
            this.path = path;
            return this;
        }

        public Configuration withData(String data) {
            this.data = data;
            return this;
        }
    }

}
