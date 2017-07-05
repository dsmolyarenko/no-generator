package org.no.generator.configuration.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class TextUtils {

    public static SearchCriteria getNameCriteria(String genderMarkerMale, String genderMarkerFemale, String languageMarker) {
        SearchCriteria sc = new SearchCriteria();
        sc.m = genderMarkerMale;
        sc.f = genderMarkerFemale;
        sc.l = languageMarker;
        return sc;
    }

    public static SearchCriteria getSurnameCriteria(String languageMarker) {
        SearchCriteria sc = new SearchCriteria();
        sc.l = languageMarker;
        return sc;
    }

    public static String[] getNames(SearchCriteria sc) {
        List<String> result = new ArrayList<>();
        readCSVResource("/db-names.csv", record -> {
            String n = record.get(0);
            String m = record.get(1);
            String f = record.get(2);
            String l = record.get(3);
            if (sc != null) {
                if (sc.m != null) {
                    if (!m.contains(sc.m)) {
                        return;
                    }
                }
                if (sc.f != null) {
                    if (!f.contains(sc.f)) {
                        return;
                    }
                }
                if (sc.l != null) {
                    if (!l.contains(sc.l)) {
                        return;
                    }
                }
            }
            result.add(n);
        });
        return result.toArray(new String[result.size()]);
    }

    public static String[] getSurnames(SearchCriteria sc) {
        List<String> result = new ArrayList<>();
        readCSVResource("/db-surnames.csv", record -> {
            String n = record.get(0);
            String l = record.get(1);
            if (sc != null) {
                if (sc.l != null) {
                    if (!l.contains(sc.l)) {
                        return;
                    }
                }
            }
            result.add(n);
        });
        return result.toArray(new String[result.size()]);
    }

    private static void readCSVResource(String resource, Consumer<CSVRecord> c) {
        try (CSVParser parser = new CSVParser(new InputStreamReader(TextUtils.class.getResourceAsStream(resource)), CSVFormat.RFC4180)) {
            parser.forEach(c);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static class SearchCriteria {
        String m, f, l;
    }
}
