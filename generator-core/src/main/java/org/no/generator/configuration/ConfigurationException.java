package org.no.generator.configuration;

public class ConfigurationException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    public ConfigurationException() {
        super();
    }

    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigurationException(String s) {
        super(s);
    }

    public ConfigurationException(Throwable cause) {
        super(cause);
    }

}
