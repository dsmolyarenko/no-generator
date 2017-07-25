package org.no.generator.configuration.spi;

import org.no.generator.Store;
import org.no.generator.configuration.ConfigurationHandlerFactoryDefault;
import org.no.generator.configuration.StoreHandlerH2;

public class ModuleStoreH2 extends ConfigurationHandlerFactoryDefault {

    public ModuleStoreH2() {
        add(Store.class, "h2",
                new StoreHandlerH2());
    }
}
