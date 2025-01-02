package DBScripts;

import Interfaces.Debuggable;
import Settings.GlobalSettings;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Delete extends ConnectDB implements Debuggable {
    private boolean isDebugMode;

    public Delete(boolean isDebugMode) {
        this.isDebugMode = GlobalSettings.isDebugMode;
    }

    public void DeleteOne(int ID) {
        try (Connection conn = ConnectDB.connect(); Statement stmt = conn.createStatement()) {
            Debug("Inside DeleteOne");
            String query = "DELETE FROM main WHERE ID = " + ID;

            stmt.executeUpdate(query);

            Debug("Deleted ID: " + ID);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void Debug(String message) {
        if (isDebugMode) {
            String className = this.getClass().getSimpleName();
            long timestamp = System.currentTimeMillis();
            System.out.println("DEBUG: [" + timestamp + "] " + className + ": " + message);
        }
    }
}
