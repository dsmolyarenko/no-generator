package org.no.generator.configuration.spi;

import org.no.generator.Generator;
import org.no.generator.Source;
import org.no.generator.configuration.ConfigurationHandlerFactoryDefault;
import org.no.generator.configuration.HandlerCase;
import org.no.generator.configuration.HandlerContext;
import org.no.generator.configuration.HandlerFormat;
import org.no.generator.configuration.HandlerJoin;
import org.no.generator.configuration.HandlerLoop;
import org.no.generator.configuration.HandlerRandomAny;
import org.no.generator.configuration.HandlerRandomDecimal;
import org.no.generator.configuration.HandlerRandomInteger;
import org.no.generator.configuration.HandlerRandomName;
import org.no.generator.configuration.HandlerRandomString;
import org.no.generator.configuration.HandlerRandomStringSet;
import org.no.generator.configuration.HandlerRandomSurname;
import org.no.generator.configuration.HandlerSequenceDecimal;
import org.no.generator.configuration.HandlerSequenceInteger;
import org.no.generator.configuration.HandlerSequenceString;
import org.no.generator.configuration.HandlerSet;
import org.no.generator.configuration.HandlerTemplate;
import org.no.generator.configuration.HandlerText;
import org.no.generator.configuration.SourceHandlerFile;
import org.no.generator.configuration.SourceHandlerRandom;
import org.no.generator.configuration.SourceHandlerWell;

public class ModuleBase extends ConfigurationHandlerFactoryDefault {

    public ModuleBase() {
        add(Generator.class, "case",
                new HandlerCase());
        add(Generator.class, "context",
                new HandlerContext());
        add(Generator.class, "set",
                new HandlerSet());
        add(Generator.class, "join",
                new HandlerJoin());
        add(Generator.class, "loop",
                new HandlerLoop());
        add(Generator.class, "text",
                new HandlerText());
        add(Generator.class, "seqInt",
                new HandlerSequenceInteger());
        add(Generator.class, "seqDec",
                new HandlerSequenceDecimal());
        add(Generator.class, "seqStr",
                new HandlerSequenceString());
        add(Generator.class, "rndAny",
                new HandlerRandomAny());
        add(Generator.class, "rndStrSet",
                new HandlerRandomStringSet());
        add(Generator.class, "rndInt",
                new HandlerRandomInteger());
        add(Generator.class, "rndDec",
                new HandlerRandomDecimal());
        add(Generator.class, "rndStr",
                new HandlerRandomString());
        add(Generator.class, "rndName",
                new HandlerRandomName());
        add(Generator.class, "rndSurname",
                new HandlerRandomSurname());
        add(Generator.class, "format",
                new HandlerFormat());
        add(Generator.class, "template",
                new HandlerTemplate());

        add(Source.class, "random",
                new SourceHandlerRandom());
        add(Source.class, "well",
                new SourceHandlerWell());
        add(Source.class, "file",
                new SourceHandlerFile());
    }
}
