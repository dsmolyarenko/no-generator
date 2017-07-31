package org.no.generator.configuration;

import org.no.generator.Writer;
import org.no.generator.GeneratorValueHolder;
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

            .register(Writer.class, "SystemTimeNanoseconds" , Generators.createSystemTimeNanoseconds())
            .register(Writer.class, "SystemTimeMicroseconds", Generators.createSystemTimeMicroseconds())
            .register(Writer.class, "SystemTimeMilliseconds", Generators.createSystemTimeMilliseconds())
            .register(Writer.class, "SystemTimeSeconds"     , Generators.createSystemTimeSeconds())
            .register(Writer.class, "SystemTimeMinutes"     , Generators.createSystemTimeMinutes())
            .register(Writer.class, "SystemTimeHours"       , Generators.createSystemTimeHours())
            .register(Writer.class, "SystemTimeDays"        , Generators.createSystemTimeDays())
            .register(Writer.class, "CurrentDate"     , Generators.createCurrentDate())
            .register(Writer.class, "CurrentTime"     , Generators.createCurrentTime())
            .register(Writer.class, "CurrentDateTime" , Generators.createCurrentDateTime())
            .register(Writer.class, "NetworkIp"   , new GeneratorValueHolder(Generators.createNetworkIp()))
            .register(Writer.class, "NetworkHost" , new GeneratorValueHolder(Generators.createNetworkHost()))
            .register(Writer.class, "NetworkMac"  , new GeneratorValueHolder(Generators.createNetworkMac()))
        ;

        DependencyContext dependencyContext = new DependencyContextDefault(system,
                new DependencyFactoryDefault(ConfigurationHandlerFactorySpi.getInstance()));

        return new ConfigurationFactory(dependencyContext);
    }
}
