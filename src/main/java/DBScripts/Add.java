package DBScripts;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Add {
    public static void normal(String inputLine) {
        try (Connection conn = ConnectDB.connect(); Statement stmt = conn.createStatement()) {
            String[] values = inputLine.split(",");

            String query = """
                    SELECT MAX(id)
                    FROM main
                    """;

            ResultSet rs = stmt.executeQuery(query);
            int id = rs.getInt(1);
            if (id == 0) {
                id = 1;
            } else {
                id++;
            }

            PreparedStatement pstmt = conn.prepareStatement("""
                    INSERT INTO main
                    VALUES(?, ?, ?, ?, ?, ?, ?, ?)
                    """);
            pstmt.setInt(1, id); // ID
            pstmt.setString(2, values[0]); // Date
            pstmt.setString(3, values[1]); // Category
            pstmt.setString(4, values[2]); // Sub-Category
            pstmt.setString(5, values[3]); // Person-Bank
            pstmt.setInt(6, Integer.parseInt(values[4])); // Sum
            pstmt.setString(7, values[5]); // Currency
            pstmt.setString(8, values[6]); // Comment

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
}
