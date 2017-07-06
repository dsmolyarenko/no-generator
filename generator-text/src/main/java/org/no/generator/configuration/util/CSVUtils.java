package org.no.generator.configuration.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class CSVUtils {

    public static String[] findBy(String resource, SearchCriteria sc) {
        List<String> result = new ArrayList<>();
        try (CSVParser parser = new CSVParser(new InputStreamReader(CSVUtils.class.getResourceAsStream(resource)), CSVFormat.RFC4180.withHeader().withSkipHeaderRecord(true))) {
            a: for (CSVRecord r : parser) {
                if (sc.restrictions != null) {
                    for (String k : sc.restrictions.keySet()) {
                        Object v = sc.restrictions.get(k);
                        if (v instanceof String) {
                            String value = (String) v;
                            if (!r.get(k).contains(value)) {
                                continue a;
                            }
                        } else if (v instanceof String[]) {
                            String[] values = (String[]) v;
                            boolean contained = false;
                            for (String value : values) {
                                if (r.get(k).contains(value)) {
                                    contained = true;
                                    break;
                                }
                            }
                            if (!contained) {
                                continue a;
                            }
                        } else {
                            throw new IllegalArgumentException("Unsupported match value type: " + v.getClass());
                        }
                    }
                }
                result.add(r.get(sc.field));
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return result.toArray(new String[result.size()]);
    }

    public static class SearchCriteria {

        private String field;

        private Map<String, Object> restrictions;

        public SearchCriteria(String field) {
            this.field = field;
        }

        public SearchCriteria matches(String field, Object value) {
            if (restrictions == null) {
                restrictions = new LinkedHashMap<>();
            }
            restrictions.put(field, value);
            return this;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
        }
    }
}
