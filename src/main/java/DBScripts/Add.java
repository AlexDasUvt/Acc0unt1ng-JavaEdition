package DBScripts;

import DBObjects.RecordData;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Add {
    public void normal(RecordData rd) {
        try (Connection conn = ConnectDB.connect(); Statement stmt = conn.createStatement()) {
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
            pstmt.setString(2, rd.date); // Date
            pstmt.setString(3, rd.category); // Category
            pstmt.setString(4, rd.subCategory); // Sub-Category
            pstmt.setString(5, rd.personBank); // Person-Bank
            pstmt.setDouble(6, rd.sum); // Sum
            pstmt.setString(7, rd.currency); // Currency
            pstmt.setString(8, rd.comment); // Comment

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
}
