package org.no.generator.configuration.spi;

import org.no.generator.Generator;
import org.no.generator.configuration.ConfigurationHandlerFactoryDefault;
import org.no.generator.configuration.HandlerRandomName;
import org.no.generator.configuration.HandlerRandomSurname;

public class ModuleText extends ConfigurationHandlerFactoryDefault {

    public ModuleText() {
        add(Generator.class, "rndName",
                new HandlerRandomName());
        add(Generator.class, "rndSurname",
                new HandlerRandomSurname());

        add(Generator.class, "TextName",
                new HandlerRandomName());
        add(Generator.class, "TextSurname",
                new HandlerRandomSurname());
    }
}
