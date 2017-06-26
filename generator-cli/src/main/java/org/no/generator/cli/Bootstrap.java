package org.no.generator.cli;

import java.io.BufferedWriter;
import java.io.Console;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.Thread.State;
import java.util.ResourceBundle;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.no.generator.Generator;
import org.no.generator.Source;
import org.no.generator.configuration.ConfigurationFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class Bootstrap {

    public static void main(String[] arguments) throws IOException, InterruptedException {

        Options options = new Options()
            .addOption(option("h", "help"  , false))
            .addOption(option("c", "config", true ))
            .addOption(option("o", "output", true ))
            .addOption(option("b", "buffer", true ));

        Configuration configuration;
        try {
            CommandLine cl = new DefaultParser().parse(options, arguments);
            if (cl.hasOption("h")) {
                help(options, null);
            }
            configuration = prepareConfiguration(
                cl.getOptionValue("c"),
                cl.getOptionValue("o", "-"),
                cl.getOptionValue("b", "8192")
            );
        } catch (ParseException e) {

            throw new Error();
        }

        ConfigurationFactory configurationFactory = ConfigurationFactory.createDefaultSpiFactory();
        configurationFactory.collect(Source.class, configuration.root.getSources());
        configurationFactory.collect(Generator.class, configuration.root.getGenerators());

        Generator generator = configurationFactory.resolve(Generator.class, configuration.root.getMain());

        Thread main = new Thread(() -> {
            try (Writer w = configuration.writer) {
                generator.append(w);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        main.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // notify about termination
            main.interrupt();

            // wait for terminated state for a second
            for (int i = 0; i < 10; i++) {
                System.err.println("await for termination...");
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (main.getState() == State.TERMINATED) {
                    break;
                }
            }
        }));
    }

    private static void help(Options options, ParseException e) {
        new HelpFormatter().printHelp(
            "generator -c configuration_file [-o output_file] [-b buffer_size]",
            e != null ? e.getMessage() : null,
            options,
            null
        );
        System.exit(128);
    }

    private static Configuration prepareConfiguration(String path, String output, String bs) throws ParseException, IOException {
        Configuration configuration = new Configuration();

        //
        // configure output writer
        //

        Writer writer;
        switch (output) {
            case "-":
                Console console = System.console();
                if (console != null) {
                    writer = console.writer();
                } else {
                    writer = new OutputStreamWriter(System.out);
                }
                break;
            case "none":
                writer = new Writer() {
                    @Override
                    public void write(char[] cbuf, int off, int len) {}

                    @Override
                    public void flush() {}

                    @Override
                    public void close() {}
                };
                break;
            default:
                Integer size;
                try {
                    size = Integer.valueOf(bs);
                } catch (NumberFormatException e) {
                    throw new ParseException("Invalid buffer size value: " + bs);
                }
                try {
                    writer = new OutputStreamWriter(new FileOutputStream(output));
                } catch (FileNotFoundException e) {
                    throw new ParseException(e.getMessage());
                }
                if (size > 0) {
                    writer = new BufferedWriter(writer, size);
                }
        }
        configuration.writer = writer;

        //
        // configure input configuration reader
        //

        if (path == null) {
            throw new ParseException("config parameter is not specified");
        }

        Reader reader;
        try {
            reader = new InputStreamReader(new FileInputStream(path));
        } catch (IOException e) {
            throw new ParseException(e.getMessage());
        }
        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        configuration.root = om.readValue(reader, Root.class);

        return configuration;
    }

    private static ResourceBundle rb = ResourceBundle.getBundle("messages");

    private static Option option(String name, String full, boolean argument) {
        return new Option(name, full, argument, "option.description." + name) {
            private static final long serialVersionUID = 1L;

            @Override
            public String getDescription() {
                return rb.getString(super.getDescription());
            }
        };
    }

    private static class Configuration {

        private Writer writer;

        private Root root;

    }

    public static class Root {

        private Object main;

        private Object[] generators;

        private Object[] sources;

        public Object getMain() {
            return main;
        }

        public void setMain(Object main) {
            this.main = main;
        }

        public Object[] getGenerators() {
            if (generators == null) {
                generators = EMPTY;
            }
            return generators;
        }

        public void setGenerators(Object[] sources) {
            this.generators = sources;
        }

        public Object[] getSources() {
            if (sources == null) {
                sources = EMPTY;
            }
            return sources;
        }

        public void setSources(Object[] sources) {
            this.sources = sources;
        }

        private static final Object[] EMPTY = new Object[0];
    }

}
