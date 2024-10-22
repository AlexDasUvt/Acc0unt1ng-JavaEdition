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
        if (dbFile.exists()) {
            dbFile.delete();
        }
        Path directoryPath = Paths.get(directory);
        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException e) {
                System.out.println("Failed to create directory: " + e.getMessage());
            }
        }
        String query = """
                    CREATE TABLE main (
                    id integer,
                    date text,
                    category text,
                    sub_category text,
                    person_bank text,
                    sum real,
                    currency text,
                    comment text
                )
                """;
        try (Connection conn = ConnectDB.connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(query);
            System.out.println("Table created successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
