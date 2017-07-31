package org.no.generator.configuration.util;

import static org.no.generator.configuration.util.ObjectUtils.getStackOwnerClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

public class ResourceUtils {

    public static InputStream openResourceAsStream(String resource) {
        URI uri;
        try {
            uri = new URI(resource);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
        InputStream in;
        if (uri.getScheme() == null) {
            in = getStackOwnerClass(1).getResourceAsStream(resource);
        } else {
            try {
                in = uri.toURL().openStream();
            } catch (IOException e) {
                in = null;
            }
        }
        return in;
    }

    public static BufferedReader openResource(String resource, Charset charset) {
        InputStream in = openResourceAsStream(resource);
        if (in == null) {
            return null;
        }
        return new BufferedReader(new InputStreamReader(in, charset));
    }

}
