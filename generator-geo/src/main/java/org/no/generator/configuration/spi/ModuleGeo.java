package org.no.generator.configuration.spi;

import org.no.generator.Generator;
import org.no.generator.configuration.ConfigurationHandlerFactoryDefault;
import org.no.generator.configuration.HandlerRandomGeoLocation;

public class ModuleGeo extends ConfigurationHandlerFactoryDefault {

    public ModuleGeo() {
        add(Generator.class, "GeoCountry",
                new HandlerRandomGeoLocation());
    }
}
