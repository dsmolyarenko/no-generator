package org.no.generator.configuration;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.no.generator.Generator;
import org.no.generator.Generators;
import org.no.generator.Source;
import org.no.generator.configuration.context.DependencyContext;
import org.no.generator.configuration.util.CSVUtils;
import org.no.generator.configuration.util.CSVUtils.SearchCriteria;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;

public final class HandlerRandomGeoLocation extends ConfigurationHandlerDefault<Generator, HandlerRandomGeoLocation.Configuration> {

    @Override
    protected Generator create(Configuration c, DependencyContext context) {

        Source source = context.get(Source.class, c.source);

        SearchCriteria searchCriteria = new SearchCriteria("geometry");
        if (c.name != null) {
            searchCriteria.matches("name", c.name);
        }
        if (c.code != null) {
            searchCriteria.matches("code", c.code);
        }

        String[] candidates = CSVUtils.findBy("/db-geo-countries.csv", searchCriteria);
        if (candidates.length == 0) {
            throw new ConfigurationException("No elements matching criteria: " + searchCriteria);
        }

        GeometryFactory gf = new GeometryFactory();

        WKBReader reader = new WKBReader(gf);

        List<Geometry> geometries = new ArrayList<>();
        for (String candidate : candidates) {
            byte[] bytes = Base64.getDecoder().decode(candidate);
            Geometry geometry;
            try {
                geometry = reader.read(bytes);
            } catch (ParseException e) {
                throw new ConfigurationException("Unable to parse geometry: " + candidate);
            }
            geometries.add(geometry);
        }

        Generator[] generators = new Generator[geometries.size()];

        int i = 0;
        for (Geometry geometry : geometries) {
            Envelope envelope = geometry.getEnvelopeInternal();
            double minX = envelope.getMinX();
            double minY = envelope.getMinY();
            double scaleX = envelope.getWidth();
            double scaleY = envelope.getHeight();
            generators[i++] = a -> {
                double x;
                double y;
                do {
                    x = minX + scaleX * source.nextDouble();
                    y = minY + scaleY * source.nextDouble();
                } while (!geometry.covers(gf.createPoint(new Coordinate(x, y))));

                a.append(Double.toString(y));
                a.append(' ');
                a.append(Double.toString(x));
            };
        }

        return Generators.createAny(generators);
    }

    public static final class Configuration {

        private Object source;

        private Object name;

        private Object code;

    }

}
