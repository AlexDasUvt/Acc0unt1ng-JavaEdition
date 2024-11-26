package DBScripts;

import DBObjects.RecordData;
import Exceptions.PBNotExist;
import Interfaces.Debuggable;

import java.sql.*;

public class Add extends ConnectDB implements Debuggable{
    private boolean isDebugMode = false;

    public Add(boolean isDebugMode) {
        this.isDebugMode = isDebugMode;
    }

    public void normal(RecordData rd) {
        try (Connection conn = ConnectDB.connect(); Statement stmt = conn.createStatement()) {
            Debug("Inside normal method");
            PreparedStatement pstmt = conn.prepareStatement("""
                    SELECT 1
                    FROM init_pb
                    WHERE person_bank = ? AND currency = ?;
                    """);
            pstmt.setString(1, rd.personBank);
            pstmt.setString(2, rd.currency);
            ResultSet rs = pstmt.executeQuery();
            if (!rs.next()) {
                throw new PBNotExist();
            }
            Debug("PB-Currency pair exists");

            String query = """
                    SELECT MAX(id)
                    FROM main
                    """;

            rs = stmt.executeQuery(query);
            int id = rs.getInt(1);
            if (id == 0) {
                id = 1;
            } else {
                id++;
            }
            Debug("New ID: " + id);

            pstmt = conn.prepareStatement("""
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

            Debug("Add successful");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void transfer(RecordData rd) {
        try (Connection conn = ConnectDB.connect(); Statement stmt = conn.createStatement()) {
            Debug("Inside transfer method");
            PreparedStatement pstmt = conn.prepareStatement("""
                    SELECT 1
                    FROM init_pb
                    WHERE person_bank = ? AND currency = ?;
                    """);
            pstmt.setString(1, rd.personBank);
            pstmt.setString(2, rd.currency);
            ResultSet rs = pstmt.executeQuery();
            if (!rs.next()) {
                // Check if sender exists
                throw new PBNotExist();
            }
            Debug("Sender PB-Currency pair exists");

            pstmt = conn.prepareStatement("""
                    SELECT 1
                    FROM init_pb
                    WHERE person_bank = ? AND currency = ?;
                    """);
            pstmt.setString(1, rd.personBankTo);
            pstmt.setString(2, rd.currency);
            rs = pstmt.executeQuery();
            if (!rs.next()) {
                // Check if receiver exists
                throw new PBNotExist();
            }
            Debug("Receiver PB-Currency pair exists");

            String query = """
                    SELECT MAX(id)
                    FROM transfer
                    """;

            rs = stmt.executeQuery(query);
            int id = rs.getInt(1);
            if (id == 0) {
                id = 1;
            } else {
                id++;
            }
            Debug("New ID: " + id);

            pstmt = conn.prepareStatement("""
                    INSERT INTO transfer
                    VALUES(?, ?, ?, ?, ?, ?, ?)
                    """);
            pstmt.setInt(1, id); // ID
            pstmt.setString(2, rd.date); // Date
            pstmt.setString(3, rd.personBank); // Person-Bank From
            pstmt.setString(4, rd.personBankTo); // Person-Bank To
            pstmt.setDouble(5, rd.sum); // Sum
            pstmt.setString(6, rd.currency); // Currency
            pstmt.setString(7, rd.comment); // Comment

            pstmt.executeUpdate();

            Debug("Transfer successful");
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
