import DBObjects.InitPBData;
import DBScripts.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.sql.*;

public class DBTest {

    @Test
    @DisplayName("Connection to Database")
    public void ConnectDB() {
        Connection conn = null;
        String url = "jdbc:sqlite:src/main/DB/Main.db";
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Assert.assertNotNull(conn);
    }

    @Test
    @DisplayName("Reading from Database")
    public void Read() {
        ResultSet rs = null;
        try (Connection conn = ConnectDB.connect(); Statement stmt = conn.createStatement()) {
            String query = "SELECT * FROM main";
            rs = stmt.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Assert.assertNotNull(rs);
    }

    @Test
    @DisplayName("Creating and Deleting Person_bank")
    public void CreateDeletePerson() {
        SPVconf conf = new SPVconf();
        InitPBData init = new InitPBData("Test1", 16.16, "TEST");
        conf.InitPB(init);
        try (Connection conn = ConnectDB.connect(); Statement stmt = conn.createStatement()) {
            String query = "SELECT person_bank FROM init_pb WHERE person_bank = 'Test1'";
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                Assert.assertEquals("Test1", rs.getString("person_bank"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Delete del = new Delete();
        del.DeletePB("Test1");
        try (Connection conn = ConnectDB.connect(); Statement stmt = conn.createStatement()) {
            PreparedStatement pstmt = conn.prepareStatement("SELECT person_bank FROM init_pb WHERE person_bank = ?");
            pstmt.setString(1, "Test1");
            ResultSet rs = pstmt.executeQuery();

            Assert.assertFalse(rs.next());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
