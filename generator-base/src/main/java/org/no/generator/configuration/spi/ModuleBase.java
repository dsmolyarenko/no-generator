package org.no.generator.configuration.spi;

import org.no.generator.Generator;
import org.no.generator.Source;
import org.no.generator.configuration.ConfigurationHandlerFactoryDefault;
import org.no.generator.configuration.HandlerFormat;
import org.no.generator.configuration.HandlerJoin;
import org.no.generator.configuration.HandlerRandomAny;
import org.no.generator.configuration.HandlerRandomInteger;
import org.no.generator.configuration.HandlerRandomNumber;
import org.no.generator.configuration.HandlerRandomSet;
import org.no.generator.configuration.HandlerRandomString;
import org.no.generator.configuration.HandlerSequenceInteger;
import org.no.generator.configuration.HandlerSequenceString;
import org.no.generator.configuration.HandlerTemplate;
import org.no.generator.configuration.SourceHandlerRandom;

public class ModuleBase extends ConfigurationHandlerFactoryDefault {

    public ModuleBase() {
        add(Generator.class, "any",
                new HandlerRandomAny());
        add(Generator.class, "format",
                new HandlerFormat());
        add(Generator.class, "join",
                new HandlerJoin());
        add(Generator.class, "seqInt",
                new HandlerSequenceInteger());
        add(Generator.class, "seqStr",
                new HandlerSequenceString());
        add(Generator.class, "rndSet",
                new HandlerRandomSet());
        add(Generator.class, "rndInt",
                new HandlerRandomInteger());
        add(Generator.class, "rndNum",
                new HandlerRandomNumber());
        add(Generator.class, "rndStr",
                new HandlerRandomString());
        add(Generator.class, "template",
                new HandlerTemplate());

        add(Source.class, "random",
                new SourceHandlerRandom());
    }
}
