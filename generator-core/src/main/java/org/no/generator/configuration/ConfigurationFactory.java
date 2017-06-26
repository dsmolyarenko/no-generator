package org.no.generator.configuration;

import org.no.generator.Generator;
import org.no.generator.Generators;
import org.no.generator.Source;
import org.no.generator.Sources;
import org.no.generator.configuration.context.DependencyContext;
import org.no.generator.configuration.context.DependencyContextDefault;
import org.no.generator.configuration.context.DependencyContextStatic;
import org.no.generator.configuration.context.DependencyFactoryDefault;
import org.no.generator.configuration.context.DependencyResolver;

public class ConfigurationFactory {

    private DependencyContext dependencyContext;

    private DependencyResolver dependencyResolver;

    public ConfigurationFactory(DependencyContext dependencyContext) {
        this.dependencyContext = dependencyContext;
        this.dependencyResolver = new DependencyResolver(dependencyContext);
    }

    public <T> void collect(Class<T> cc, Object... configurations) {
        dependencyResolver.collect(cc, configurations);
    }

    public <T> T resolve(Class<T> cc, Object configuration) {
        dependencyResolver.resolve();
        return dependencyContext.get(cc, configuration);
    }

    public static ConfigurationFactory createDefaultSpiFactory() {

        DependencyContext system = new DependencyContextStatic()
            .register(Source.class, "default", Sources.createDefault())

            .register(Generator.class, "SystemTimeNanoseconds" , Generators.createSystemTimeNanoseconds())
            .register(Generator.class, "SystemTimeMicroseconds", Generators.createSystemTimeMicroseconds())
            .register(Generator.class, "SystemTimeMilliseconds", Generators.createSystemTimeMilliseconds())
            .register(Generator.class, "SystemTimeSeconds"     , Generators.createSystemTimeSeconds())
            .register(Generator.class, "SystemTimeMinutes"     , Generators.createSystemTimeMinutes())
            .register(Generator.class, "SystemTimeHours"       , Generators.createSystemTimeHours())
            .register(Generator.class, "SystemTimeDays"        , Generators.createSystemTimeDays())
            .register(Generator.class, "CurrentDate"     , Generators.createCurrentDate())
            .register(Generator.class, "CurrentTime"     , Generators.createCurrentTime())
            .register(Generator.class, "CurrentDateTime" , Generators.createCurrentDateTime())
            .register(Generator.class, "NetworkIp"   , Generators.createNetworkIp())
            .register(Generator.class, "NetworkHost" , Generators.createNetworkHost())
            .register(Generator.class, "NetworkMac"  , Generators.createNetworkMac())
        ;

        DependencyContext dependencyContext = new DependencyContextDefault(system,
                new DependencyFactoryDefault(ConfigurationHandlerFactorySpi.getInstance()));

        return new ConfigurationFactory(dependencyContext);
    }
}
