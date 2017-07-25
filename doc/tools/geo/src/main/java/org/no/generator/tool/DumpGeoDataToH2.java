package org.no.generator.tool;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.h2.tools.RunScript;
import org.h2.tools.Script;
import org.h2.util.IOUtils;
import org.h2.util.ScriptReader;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

public class DumpGeoDataToH2 {

    private static String DB_NAME = "db-gl.sql";;

    public static void main(String[] args) throws Throwable {
        new DumpGeoDataToH2().main(Configuration.of(args));
    }

    private void main(Configuration c) throws Throwable {
        String sUrl = "jdbc:sqlite:" + c.d;
        String oUrl = "jdbc:h2:mem:public;";

        String schema = getClass().getSimpleName() + ".sql";

        try (
            Connection sConnection = DriverManager.getConnection(sUrl);
            Connection oConnection = DriverManager.getConnection(oUrl);
        ) {
            importChema(c, schema, oConnection);
            dumpCountries(sConnection, oConnection);
            dumpCities(sConnection, oConnection);
            export(c, schema, oConnection);
        }
    }

    private void importChema(Configuration c, String schema, Connection connection) throws SQLException {
        Reader schemaReader = IOUtils.getReader(getClass().getResourceAsStream(schema));
        RunScript.execute(connection, schemaReader);
    }

    private void dumpCountries(Connection sc, Connection oc) throws Throwable {

        try (Statement ss = sc.createStatement();
            ResultSet sr = ss.executeQuery("select admin as country, case when iso_a2 != '-99' then iso_a2 else null end as code, continent, region_un as region, "
                    + "case when pop_est != -99.0 then pop_est else null end as population, geometry from `ne_10m_admin_0_countries_lakes` order by region_un, continent, iso_a2 ");
            PreparedStatement ps = oc.prepareStatement("insert into Country (id, country, code, continent, region, population, geometry) values (?, ?, ?, ?, ?, ?, ?);");
        ) {
            int count = copy(sr, ps);
            System.out.println("Countries: copied " + count + " row" + ((count != 1) ? "s" : ""));
        }
    }

    private void dumpCities(Connection sc, Connection oc) throws Throwable {

        try (Statement st = sc.createStatement();
            ResultSet sr = st.executeQuery("select name as city, adm0name as country, case when iso_a2 != '-99' then iso_a2 else null end as code, "
                    + "case when gn_pop != -99.0 then gn_pop else null end as population, latitude, longitude, geometry "
                    + "from `ne_10m_populated_places` order by adm0name, adm1name");
            PreparedStatement ps = oc.prepareStatement("insert into City "
                    + "(id, city, country, code, population, latitude, longitude, geometry) values (?, ?, ?, ?, ?, ?, ?, ?)");
        ) {
            int count = copy(sr, ps);
            System.out.println("Cities: copied " + count + " row" + ((count != 1) ? "s" : ""));
        }
    }

    private int copy(ResultSet rs, PreparedStatement ps) throws SQLException {
        int id = 1;
        while (rs.next()) {
            ps.setObject(1, id++);
            for (int index = 1; index <= rs.getMetaData().getColumnCount(); index++) {
                ps.setObject(index + 1, rs.getObject(index));
            }
            ps.execute();
        }
        return id - 1;
    }

    private void export(Configuration c, String schema, Connection oConnection)
            throws IOException, SQLException {
        File dump = File.createTempFile("dump", null);
        Script.process(oConnection, dump.getPath(), "", "");

        try (PrintStream out = new PrintStream(new File(c.o, DB_NAME));
            ScriptReader sr = new ScriptReader(IOUtils.getReader(new FileInputStream(dump)));
        ) {
            // append schema
            IOUtils.copy(getClass().getResourceAsStream(schema), out);
            out.println();
            // append data
            String statement;
            while ((statement = sr.readStatement()) != null) {
                statement = statement.trim();
                if (statement.startsWith("INSERT INTO")) {
                    out.println(statement + ';');
                }
            }
        }
    }

    private static class Configuration {

        @Parameter(names = { "-d" }, description = "Source database path", required = true)
        private String d;

        @Parameter(names = { "-o" }, description = "Output database dump folder")
        private String o = ".";

        public static Configuration of(String[] args) throws IOException {
            Configuration c = new Configuration();

            JCommander jc = new JCommander(c);
            try {
                jc.parse(args);
            } catch (ParameterException e) {
                System.err.println(e.getMessage());
                jc.usage();
                System.exit(128);
            }

            return c;
        }

    }
}
