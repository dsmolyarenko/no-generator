package org.no.generator.configuration;

import java.awt.Point;
import java.text.MessageFormat;
import java.util.Map;

import org.no.generator.Generator;
import org.no.generator.Source;
import org.no.generator.configuration.context.DependencyContext;

import com.vividsolutions.jts.geom.Geometry;

public final class HandlerRandomLocation extends ConfigurationHandlerDefault<Generator, HandlerRandomLocation.Configuration> {

    @Override
    protected Generator create(Configuration c, DependencyContext context) {

        MessageFormat mf = new MessageFormat(c.format);

        Source source = context.get(Source.class, c.source);

        return null;

//        SearchCriteria searchCriteria = new SearchCriteria();
//        if (c.name != null) {
//            searchCriteria.matches("Name", c.name);
//        }
//        if (c.code != null) {
//            searchCriteria.matches("Code", c.code);
//        }
//
//        List<CSVRecord> countries = CSVUtils2.findRecordsBy("/db-geo-countries.csv", searchCriteria);
//        if (countries.size() == 0) {
//            throw new ConfigurationException("No elements matching criteria: " + searchCriteria);
//        }
//
//        GeometryFactory gf = new GeometryFactory();
//
//        WKBReader reader = new WKBReader(gf);
//
//        List<Tuple> tuples = new ArrayList<>();
//        for (CSVRecord country : countries) {
//            Tuple tuple = new Tuple();
//
//            byte[] bytes = Base64.getDecoder().decode(country.get("Geometry"));
//            Geometry geometry;
//            try {
//                geometry = reader.read(bytes);
//            } catch (ParseException e) {
//                throw new ConfigurationException("Unable to parse geometry: " + country);
//            }
//            tuple.geometry = geometry;
//
//            tuple.population = Double.valueOf(country.get("Population")).intValue();
//
//            Map<Point, Integer> cityTuple = new HashMap<>();
//            List<CSVRecord> cities = CSVUtils2.findRecordsBy("/db-geo-cities.csv", new SearchCriteria()
//                .matches("Code", country.get("Code"))
//                .matches("Name", c.city)
//            );
//            for (CSVRecord city : cities) {
//                cityTuple.put(gf.createPoint(new Coordinate(Double.valueOf(city.get("Longitude")), Double.valueOf(city.get("Latitude")))),
//                        Integer.valueOf(city.get("Population")));
//            }
//            tuple.cities = cityTuple;
//
//            tuples.add(tuple);
//        }
//
//
//        Random random = new Random();
//
//        List<Integer> l0 = new ArrayList<>();
//        List<List<Generator>> l1 = new ArrayList<>();
//        List<List<Integer>> l2 = new ArrayList<>();
//
//        for (Tuple tuple : tuples) {
//            List<Generator> generators = new ArrayList<>();
//            List<Integer> weights = new ArrayList<>();
//
//            l0.add(tuple.population);
//            l1.add(generators);
//            l2.add(weights);
//
//            Envelope envelope = tuple.geometry.getEnvelopeInternal();
//            double scaleX = envelope.getWidth();
//            double scaleY = envelope.getHeight();
//
//            double scaleA = (scaleX + scaleY) / 2 / tuple.population;
//
//            for (Point cityPoint : tuple.cities.keySet()) {
//                Integer cityPopulation = tuple.cities.get(cityPoint);
//                if (!tuple.geometry.covers(cityPoint)) {
//                    System.err.println("warning: ");
//                    continue;
//                }
//                weights.add(cityPopulation);
//
//                generators.add(appender -> {
//                    double x = 0;
//                    double y = 0;
//
//                    boolean done = false;
//                    for (int i = 0; i < 10; i++) {
//                        double r = scaleA * cityPopulation * source.nextDouble();
//                        double a = Math.PI * 2 * random.nextDouble();
//
//                        x = cityPoint.getX() + r * Math.cos(a);
//                        y = cityPoint.getY() + r * Math.sin(a);
//
//                        if (tuple.geometry.covers(gf.createPoint(new Coordinate(x, y)))) {
//                            done = true;
//                            break;
//                        }
//
//                    }
//                    if (!done) {
//                        x = cityPoint.getX();
//                        y = cityPoint.getY();
//                    }
//
//                    appender.append(mf.format(new Object[] { y, x }));
//                });
//            }
//        }
//
//        int         t0 = 0;
//        Generator[] g0 = new Generator[l0.size()];
//        int[]       w0 = new int[l0.size()];
//
//        for (int i = 0; i < l0.size(); i++) {
//            int             x0 = l0.get(i);
//            List<Generator> x1 = l1.get(i);
//            List<Integer>   x2 = l2.get(i);
//
//            t0 += x0;
//
//            int[] d = new int[x2.size()];
//            for (int j = 0; j < d.length; j++) {
//                d[j] = x2.get(j);
//            }
//            g0[i] = Generators.createAny(x1.toArray(new Generator[0]), x0, d);
//            w0[i] = x0;
//        }
//
//        return Generators.createAny(g0, t0, w0);


//            Envelope envelope = tuple.geometry.getEnvelopeInternal();
//            double scaleX = envelope.getWidth();
//            double scaleY = envelope.getHeight();
//
//            for (Point cityPoint : tuple.cities.keySet()) {
//                Integer cityPopulation = tuple.cities.get(cityPoint);
//
//                double scaleXCity = scaleX * cityPopulation / tuple.population;
//                double scaleYCity = scaleY * cityPopulation / tuple.population;
//
//                double minX = cityPoint.getX() - scaleXCity / 2;
//                double minY = cityPoint.getY() - scaleYCity / 2;
//
//                generators.add(a -> {
//                    double x = 0;
//                    double y = 0;
//
//                    boolean done = false;
//                    for (int i = 0; i < 10; i++) {
//                        x = minX + scaleXCity * source.nextDouble();
//                        y = minY + scaleYCity * source.nextDouble();
//                        if (tuple.geometry.covers(gf.createPoint(new Coordinate(x, y)))) {
//                            done = true;
//                            break;
//                        }
//                    }
//                    if (!done) {
//                        x = cityPoint.getX();
//                        y = cityPoint.getY();
//                    }
//                    a.append("{");
//                    a.append("\"lat\": ");
//                    a.append(Double.toString(y));
//                    a.append(", ");
//                    a.append("\"lon\": ");
//                    a.append(Double.toString(x));
//                    a.append("}");
//                });
//            }
//        }

//        return Generators.createAny(generators.toArray(new Generator[generators.size()]));
    }

    public static final class Configuration {

        private Object source;

        private Object name;

        private Object code;

        private Object city;

        private String format = "{0} {1}";

    }

    public static class Tuple {

        private Geometry geometry;

        private Integer population;

        private Map<Point, Integer> cities;

    }

}
