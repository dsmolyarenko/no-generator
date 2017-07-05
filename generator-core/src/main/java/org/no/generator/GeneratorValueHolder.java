package org.no.generator;

import java.io.IOException;

public class GeneratorValueHolder implements Generator {

    private final Generator generator;

    private String value;

    public GeneratorValueHolder(Generator generator) {
        super();
        this.generator = generator;
    }

    public String getValue() {
        return value;
    }

    public void reset() {
        value = null;
    }

    @Override
    public void append(Appendable a) throws IOException {
        if (value == null) {
            value = read(a);
        }
        a.append(value);
    }

    private String read(Appendable a) throws IOException {
        StringBuilder sb = new StringBuilder();
        generator.append(sb);
        return sb.toString();
    }

}
