package org.no.generator.cli.util;

import java.io.IOException;
import java.io.Reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class Utils {

    public static <T> T read(Reader reader, Class<T> c) throws IOException {
        return new ObjectMapper(new YAMLFactory()).readValue(reader, c);
    }

}
