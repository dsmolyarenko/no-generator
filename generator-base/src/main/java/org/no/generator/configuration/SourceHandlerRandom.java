package org.no.generator.configuration;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;

import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.RandomGeneratorFactory;
import org.apache.commons.math3.random.Well1024a;
import org.apache.commons.math3.random.Well19937a;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.random.Well44497a;
import org.apache.commons.math3.random.Well44497b;
import org.apache.commons.math3.random.Well512a;
import org.no.generator.Source;
import org.no.generator.SourceUtils;
import org.no.generator.configuration.context.DependencyContext;
import org.no.generator.configuration.util.ObjectUtils;

public class SourceHandlerRandom implements ConfigurationHandler<Source> {

    @Override
    public Source handle(Map<String, Object> configuration, DependencyContext context) {
        Configuration c = ObjectUtils.map(configuration, Configuration.class);

        RandomGenerator rg;

        switch (c.source) {
            case "default":
                rg = RandomGeneratorFactory.createRandomGenerator(new Random());
                break;
            case "secure":
                rg = RandomGeneratorFactory.createRandomGenerator(new SecureRandom());
                break;
            case "well1024a":
                rg = new Well1024a();
                break;
            case "well19937a":
                rg = new Well19937a();
                break;
            case "well19937c":
                rg = new Well19937c();
                break;
            case "well44497a":
                rg = new Well44497a();
                break;
            case "well44497b":
                rg = new Well44497b();
                break;
            case "well512a":
                rg = new Well512a();
                break;
            default:
                throw new IllegalArgumentException("Unsupported random generator type: " + c.source);
        }

        return SourceUtils.createDefault(rg, c.distribution);
    }

    public static final class Configuration {

        private String source = "default";

        private double[] distribution;

    }

}
