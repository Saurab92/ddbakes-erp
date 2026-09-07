package com.bakery.inventory.util;

import java.util.List;
import java.util.function.Function;

/**
 * Minimal CSV serialization helper for report exports. Avoids pulling in an
 * external CSV dependency for the small, fixed-column reports in this API.
 */
public final class CsvExportUtil {

    private CsvExportUtil() {
    }

    /**
     * Builds a CSV document (with header row) from a list of rows, using the
     * given column extractors.
     *
     * @param headers    column header names, in order
     * @param rows       data rows
     * @param extractors one value-extractor per header, in the same order
     * @param <T>        row type
     * @return the CSV document as a single string, using CRLF line endings
     */
    @SafeVarargs
    public static <T> String toCsv(List<String> headers, List<T> rows, Function<T, Object>... extractors) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.join(",", headers.stream().map(CsvExportUtil::escape).toArray(String[]::new)));
        sb.append("\r\n");

        for (T row : rows) {
            StringBuilder line = new StringBuilder();
            for (int i = 0; i < extractors.length; i++) {
                if (i > 0) {
                    line.append(",");
                }
                Object value = extractors[i].apply(row);
                line.append(escape(value == null ? "" : value.toString()));
            }
            sb.append(line).append("\r\n");
        }
        return sb.toString();
    }

    private static String escape(String value) {
        if (value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
