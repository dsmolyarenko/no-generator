package org.no.generator;

import java.io.IOException;

public interface Generator {

    void append(Appendable a) throws IOException;

}
