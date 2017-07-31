package org.no.generator;

public class DataSourceExhaustedException extends IllegalStateException {

    private static final long serialVersionUID = 1L;

    public DataSourceExhaustedException() {
        super();
    }

    public DataSourceExhaustedException(String s) {
        super(s);
    }
}