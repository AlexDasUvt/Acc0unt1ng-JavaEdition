package DBScripts;

import DBObjects.ResultData;
import Enums.ReadCode;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.*;

public class Read {
    String query;

    public static ResultData ReadDB(ReadCode mode) {
        List<Map<String, Object>> list = new ArrayList<>();
        String query = null;

        if (mode == ReadCode.allm) {
            query = ("""
                        SELECT *
                        FROM main
                    """);
        } else if (mode == ReadCode.mplus) {
            query = ("""
                    SELECT *
                    FROM main
                    WHERE sum > 0
                    """);
        } else if (mode == ReadCode.mminus) {
            query = ("""
                    SELECT *
                    FROM main
                    WHERE sum < 0
                    """);
        } else if (mode == ReadCode.tran) {
            query = ("""
                    SELECT *
                    FROM transfer
                    """);
        } else if (mode == ReadCode.inits) {
            query = ("""
                    SELECT *
                    FROM init_pb
                    """);
        } else if (mode == ReadCode.balance) {
            query = ("""
                    SELECT person_bank, SUM(total_sum) AS total_balance, currency
                    FROM (
                        -- Step 1: Summing the balances from main table and init_pb table
                        SELECT person_bank, SUM(sum) AS total_sum, currency
                        FROM main
                        GROUP BY person_bank, currency
                    
                        UNION ALL
                    
                        SELECT person_bank, SUM(sum) AS total_sum, currency
                        FROM init_pb
                        GROUP BY person_bank, currency
                    
                        UNION ALL
                    
                        -- Step 2: Add incoming transfers (person_bank_to)
                        SELECT person_bank_to AS person_bank, SUM(sum) AS total_sum, currency
                        FROM transfer
                        GROUP BY person_bank_to, currency
                    
                        UNION ALL
                    
                        -- Step 3: Subtract outgoing transfers (person_bank_from)
                        SELECT person_bank_from AS person_bank, -SUM(sum) AS total_sum, currency
                        FROM transfer
                        GROUP BY person_bank_from, currency
                    ) AS combined_sums
                    GROUP BY person_bank, currency;
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
        return new ResultData(list);
    }

    public static ReadCode getReadCode(String mode) {
        ReadCode rc = null;
        switch (mode) {
            case "allm":
                rc = ReadCode.allm;
                break;
            case "m+":
                rc = ReadCode.mplus;
                break;
            case "m-":
                rc = ReadCode.mminus;
                break;
            case "init":
                rc = ReadCode.inits;
                break;
            case "bal":
                rc = ReadCode.balance;
                break;
            case "tran":
                rc = ReadCode.tran;
                break;
            default:
                System.out.println("Invalid mode!");
                throw new Error("Invalid mode!");
        }
        return rc;
    }
}
