package DBScripts;

import Interfaces.Debuggable;
import Settings.GlobalSettings;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Delete extends ConnectDB implements Debuggable {
    private final boolean isDebugMode;

    public Delete() {
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

    public void DeletePB(String PB) {
        try (Connection conn = ConnectDB.connect(); Statement stmt = conn.createStatement()) {
            Debug("Inside DeletePB");
            String query = "DELETE FROM init_pb WHERE person_bank = '" + PB + "'";

            stmt.executeUpdate(query);
            Debug("Deleted PB from InitPB: '" + PB + "'");
            query = "DELETE FROM main WHERE person_bank = '" + PB + "'";
            stmt.executeUpdate(query);
            Debug("Deleted PB from Main: " + PB);
            query = "DELETE FROM transfer WHERE person_bank_from = '" + PB + "' OR person_bank_to = '" + PB + "'";
            stmt.executeUpdate(query);
            Debug("Deleted PB from Transfer: " + PB);
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
