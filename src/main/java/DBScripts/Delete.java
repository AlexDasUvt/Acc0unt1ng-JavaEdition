package DBScripts;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Delete {
    public void DeleteOne(int ID) {
        try (Connection conn = ConnectDB.connect(); Statement stmt = conn.createStatement()) {
            String query = "DELETE FROM users WHERE ID = " + ID;

            stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
