package org.no.generator.configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;

import org.h2.tools.RunScript;
import org.h2.util.IOUtils;
import org.no.generator.Store;
import org.no.generator.configuration.context.DependencyContext;

public class StoreHandlerH2 extends ConfigurationHandlerDefault<Store, StoreHandlerH2.Configuration> {

    @Override
    protected Store create(Configuration c, DependencyContext context) {

        if (c.query == null) {
            throw new ConfigurationException("Field `query` should be specified");
        }

        String url = "jdbc:h2:" + c.specifications;

        Connection connection;
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new ConfigurationException("Unable to create DB connection: " + url, e);
        }

        if (c.imports != null) {
            for (String i : c.imports) {
                try {
                    RunScript.execute(connection, IOUtils.getReader(getClass().getResourceAsStream(i)));
                } catch (SQLException e) {
                    throw new ConfigurationException("Unable to create import data: " + i, e);
                }
            }
        }

        Object[] data;
        try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(c.query);) {
            List<Object> result = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> object = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
                for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                    object.put(rs.getMetaData().getColumnName(i + 1).toLowerCase().intern(), rs.getObject(i + 1));
                }
                result.add(object);
            }
            data = result.toArray(new Object[result.size()]);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        int count = data.length;
        logger.log(Level.INFO, () -> getClass() + ": stored " + count + " item" + (count != 1 ? "s" : ""));

        return () -> {
            return data;
        };
    }

    public static final class Configuration {

        private String[] imports;

        private String specifications = "mem:";

        private String query;

        public Configuration() {

        }

        public Configuration withImports(String... imports) {
            this.imports = imports;
            return this;
        }

        public Configuration withSpecifications(String specifications) {
            this.specifications = specifications;
            return this;
        }

        public Configuration withQuery(String query) {
            this.query = query;
            return this;
        }

    }

}
