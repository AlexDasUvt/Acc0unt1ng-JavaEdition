package DBScripts;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class NewDB {
    static String directory = new File("src/main/DB/Main.db").getParent();

    public static void createTable() {
        File dbFile = new File("src/main/DB/Main.db");

        // Close any existing database connection if required before deleting the DB file
        try {
            if (dbFile.exists()) {
                // Ensure file is deleted
                if (!dbFile.delete()) {
                    System.out.println("Failed to delete the existing database file.");
                    return;
                } else {
                    System.out.println("Old database file deleted successfully.");
                }
            }
        } catch (SecurityException e) {
            System.out.println("Error deleting database file: " + e.getMessage());
            return;
        }

        // Ensure the directory exists or create it
        Path directoryPath = Paths.get(directory);
        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException e) {
                System.out.println("Failed to create directory: " + e.getMessage());
                return;
            }
        }

        String mainTable = """
                CREATE TABLE main (
                    id integer PRIMARY KEY,
                    date text,
                    category text,
                    sub_category text,
                    person_bank text,
                    sum real,
                    currency text,
                    comment text
                );
                """;
        String initTable = """
                CREATE TABLE init_pb (
                    person_bank text,
                    sum real,
                    currency text
                );
                """;
        String transferTable = """
                CREATE TABLE transfer (
                    id integer,
                    date text,
                    person_bank_from text,
                    person_bank_to text,
                    sum real,
                    currency text,
                    comment text
                );""";

        try (Connection conn = ConnectDB.connect(); Statement stmt = conn.createStatement()) {
            stmt.addBatch(mainTable);
            stmt.addBatch(initTable);
            stmt.addBatch(transferTable);

            stmt.executeBatch();
            System.out.println("Database and tables created successfully.");
        } catch (SQLException e) {
            System.out.println("Error creating tables: " + e.getMessage());
        }
    }
}
