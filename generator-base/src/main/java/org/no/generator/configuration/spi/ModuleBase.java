package org.no.generator.configuration.spi;

import org.no.generator.Sequence;
import org.no.generator.Source;
import org.no.generator.Store;
import org.no.generator.Writer;
import org.no.generator.configuration.ConfigurationHandlerFactoryDefault;
import org.no.generator.configuration.HandlerCase;
import org.no.generator.configuration.HandlerContext;
import org.no.generator.configuration.HandlerDecimal;
import org.no.generator.configuration.HandlerHttp;
import org.no.generator.configuration.HandlerInteger;
import org.no.generator.configuration.HandlerJoin;
import org.no.generator.configuration.HandlerLoop;
import org.no.generator.configuration.HandlerSize;
import org.no.generator.configuration.HandlerSource;
import org.no.generator.configuration.HandlerString;
import org.no.generator.configuration.HandlerStringSet;
import org.no.generator.configuration.HandlerSwitch;
import org.no.generator.configuration.HandlerTemplate;
import org.no.generator.configuration.HandlerText;
import org.no.generator.configuration.SequenceHandlerFile;
import org.no.generator.configuration.SequenceHandlerSequential;
import org.no.generator.configuration.SequenceHandlerWell;
import org.no.generator.configuration.SourceHandlerStore;
import org.no.generator.configuration.SourceHandlerTransformer;
import org.no.generator.configuration.StoreHandlerCsv;
import org.no.generator.configuration.StoreHandlerSimple;

public class ModuleBase extends ConfigurationHandlerFactoryDefault {

    public ModuleBase() {
        add(Writer.class, "http",
                new HandlerHttp());
        add(Writer.class, "size",
                new HandlerSize());
        add(Writer.class, "case",
                new HandlerCase());
        add(Writer.class, "context",
                new HandlerContext());
        add(Writer.class, "loop",
                new HandlerLoop());
        add(Writer.class, "join",
                new HandlerJoin());

        add(Writer.class, "text",
                new HandlerText());

        add(Writer.class, "Integer",
                new HandlerInteger());
        add(Writer.class, "Decimal",
                new HandlerDecimal());
        add(Writer.class, "String",
                new HandlerString());

        add(Writer.class, "StringSet",
                new HandlerStringSet()); // make it ObjectSet

        //
        // Writers
        //

        add(Writer.class, "Switch",
                new HandlerSwitch());
        add(Writer.class, "Format",
                new HandlerTemplate());
        add(Writer.class, "Source",
                new HandlerSource());

        //
        // Sequences
        //

        add(Sequence.class, "sequential",
                new SequenceHandlerSequential());
        add(Sequence.class, "random",
                new SequenceHandlerSequential());
        add(Sequence.class, "well",
                new SequenceHandlerWell());
        add(Sequence.class, "file",
                new SequenceHandlerFile());

        //
        // Sources
        //

        add(Source.class, "Store",
                new SourceHandlerStore());
        add(Source.class, "Transformer",
                new SourceHandlerTransformer());

        //
        // Stores
        //

        add(Store.class, "Simple",
                new StoreHandlerSimple());
        add(Store.class, "Csv",
                new StoreHandlerCsv());

    }
}
