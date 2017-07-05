package org.no.generator.configuration;

import java.util.HashMap;
import java.util.Map;

import org.no.generator.Generator;
import org.no.generator.GeneratorValueHolder;
import org.no.generator.configuration.context.DependencyContext;
import org.no.generator.configuration.util.ObjectUtils;

public final class HandlerContext extends ConfigurationHandlerDefault<Generator, HandlerContext.Configuration> {

    @Override
    protected Generator create(Configuration c, DependencyContext context) {

        Map<String, GeneratorValueHolder> holders = new HashMap<>();

        DependencyContext contextWrapper = new DependencyContext() {
            @Override
            public <T> T get(Class<T> type, Object o, DependencyContext ctx) {
                if (o instanceof String) {
                    if (type == Generator.class) {
                        String id = (String) o;
                        if (ObjectUtils.in(id, c.hold)) {
                            GeneratorValueHolder holder = holders.get(id);
                            if (holder == null) {
                                holders.put(id, holder = new GeneratorValueHolder(context.get(Generator.class, id)));
                            }
                            return ObjectUtils.safe(holder);
                        }
                    }
                }
                return context.get(type, o, ctx);
            }
        };

        Generator generator = context.get(Generator.class, c.sample, contextWrapper);

        return a -> {
            generator.append(a);
            // reset current stack cache context
            for (GeneratorValueHolder h : holders.values()) {
                h.reset();
            }
        };
    }

    public static final class Configuration {

        private Object sample;

        private String[] hold;

    }

}
