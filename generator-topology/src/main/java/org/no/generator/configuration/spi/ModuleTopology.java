package org.no.generator.configuration.spi;

import org.no.generator.Generator;
import org.no.generator.configuration.ConfigurationHandlerFactoryDefault;
import org.no.generator.configuration.HandlerRandomLocation;

public class ModuleTopology extends ConfigurationHandlerFactoryDefault {

    public ModuleTopology() {
        add(Generator.class, "location",
                new HandlerRandomLocation());

    }
}
