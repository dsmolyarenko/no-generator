package org.no.generator.configuration.spi;

import org.no.generator.Store;
import org.no.generator.configuration.ConfigurationHandlerFactoryDefault;
import org.no.generator.configuration.StoreHandlerLucene;

public class ModuleLucene extends ConfigurationHandlerFactoryDefault {

    public ModuleLucene() {
        add(Store.class, "Lucene",
                new StoreHandlerLucene());
    }
}
