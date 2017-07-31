package org.no.generator;

public interface Source {

    Object next() throws DataSourceExhaustedException;

}
