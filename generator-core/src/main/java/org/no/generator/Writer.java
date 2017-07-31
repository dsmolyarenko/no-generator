package org.no.generator;

import java.io.IOException;

public interface Writer {

    void append(Appendable a) throws IOException;

}
