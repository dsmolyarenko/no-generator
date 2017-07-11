package org.no.generator.tool;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Base64;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

public class DumpBorders {

    public static void main(String[] args) throws Throwable {
        main(Configuration.of(args));
    }

    private static void main(Configuration c) throws Throwable {
        String url = "jdbc:sqlite:" + c.d;

        CSVPrinter printer = new CSVPrinter(c.writer, CSVFormat.RFC4180);

        // print header
        printer.printRecord("id", "name", "code", "continent", "geometry");

        try (Connection connection = DriverManager.getConnection(url);
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery("select * from `ne_10m_admin_0_countries`")
        ) {
            while (rs.next()) {
                printer.printRecord(
                    rs.getInt("OGC_FID"),
                    rs.getString("subunit"),
                    rs.getString("su_a3"),
                    rs.getString("continent"),
                    new String(Base64.getEncoder().encode(rs.getBytes("geometry")))
                );
            }
        }

        printer.close();
    }

    private static class Configuration {

        @Parameter(names = { "-d" }, description = "Database path", required = true)
        private String d;

        @Parameter(names = { "-o" }, description = "Output file")
        private String o = "-";

        private Writer writer;

        public static Configuration of(String[] args) throws IOException {
            Configuration c = new Configuration();

            JCommander jc = new JCommander(c);
            try {
                jc.parse(args);

                c.writer = new OutputStreamWriter(c.o.equals("-")
                    ? System.out
                    : new FileOutputStream(c.o));

            } catch (ParameterException e) {
                System.err.println(e.getMessage());
                jc.usage();
                System.exit(128);
            }

            return c;
        }

    }
}
