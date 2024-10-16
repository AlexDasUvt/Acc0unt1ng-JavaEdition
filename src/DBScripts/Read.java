package DBScripts;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.*;

public class Read {
    String query;

    public static List<Map<String, Object>> ReadDB(String mode) {
        List<Map<String, Object>> list = new ArrayList<>();
        String query = null;

        if (Objects.equals(mode, "allm")) {
            query = ("""
                        SELECT *
                        FROM main
                    """);
        } else if (Objects.equals(mode, "m+")) {
            query = ("""
                    SELECT *
                    FROM main
                    WHERE sum > 0
                    """);
        } else if (Objects.equals(mode, "m-")) {
            query = ("""
                    SELECT *
                    FROM main
                    WHERE sum < 0
                    """);
        }

        try (Connection conn = ConnectDB.connect(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = rsmd.getColumnName(i);
                    Object columnValue = rs.getObject(i);
                    row.put(columnName, columnValue);
                }
                list.add(row);
            }
        } catch (
                Exception e) {
            System.out.println(e.getMessage());
        }
        return list;
    }
}
