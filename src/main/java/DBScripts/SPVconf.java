package DBScripts;

import DBObjects.InitPBData;
import Interfaces.Debuggable;
import Settings.GlobalSettings;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SPVconf extends ConnectDB implements Debuggable {
    protected String catPath;
    protected String subcatPath;
    protected String currPath;
    boolean isDebugMode;

    public SPVconf() {
        this.catPath = "src/main/DB/cat.txt";
        this.subcatPath = "src/main/DB/subcat.txt";
        this.currPath = "src/main/DB/curr.txt";
        this.isDebugMode = GlobalSettings.isDebugMode;
    }

    public void WriteSPV(String pth, String newSPV, String inpMode) {
        Debug("Inside WriteSPV. Running with: " + pth + ", " + newSPV + ", " + inpMode);
        FileWriter fw = null;
        PrintWriter pw = null;
        newSPV = newSPV.replace(",", "\n");

        try {
            if (pth.equalsIgnoreCase("cat")) {
                fw = new FileWriter(catPath, true);
            } else if (pth.equalsIgnoreCase("subcat")) {
                fw = new FileWriter(subcatPath, true);
            } else if (pth.equalsIgnoreCase("curr")) {
                fw = new FileWriter(currPath, true);
            } else {
                throw new Exception("Invalid path: " + pth);
            }

            if (inpMode.equalsIgnoreCase("a")) {
                pw = new PrintWriter(fw, true);
            } else if (inpMode.equalsIgnoreCase("r")) {
                pw = new PrintWriter(fw);
            } else {
                throw new RuntimeException("Invalid input mode: " + inpMode);
            }

            pw.write(newSPV);
            pw.close();
            fw.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String ReadSPV(String pth) {
        Debug("Inside ReadSPV. Running with: " + pth);
        BufferedReader br = null;
        StringBuilder result = new StringBuilder();
        String line;

        try {
            if (pth.equalsIgnoreCase("cat")) {
                br = new BufferedReader(new FileReader(catPath));
            } else if (pth.equalsIgnoreCase("subcat")) {
                br = new BufferedReader(new FileReader(subcatPath));
            } else if (pth.equalsIgnoreCase("curr")) {
                br = new BufferedReader(new FileReader(currPath));
            } else {
                throw new Exception("Invalid path: " + pth);
            }

            // Read each line from the file and append it to the result
            while ((line = br.readLine()) != null) {
                result.append(line).append("\n");
            }

            br.close();

            // Remove the last newline character if present
            if (!result.isEmpty()) {
                result.setLength(result.length() - 1);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return result.toString();
    }

    public List<String> readSPVList(String pth){
        String normalText = ReadSPV(pth);
        List<String> result = new ArrayList<String>();

        for (String line : normalText.split("\n")) {
            result.add(line);
        }
        return result;
    }

    public String InitPB(InitPBData pbData) {
        Debug("Inside InitPB");
        try (Connection conn = ConnectDB.connect(); Statement stmt = conn.createStatement()) {
            PreparedStatement pstmt = conn.prepareStatement("""
                    INSERT INTO init_pb
                    VALUES(?, ?, ?)
                    """);
            pstmt.setString(1, pbData.PersonBank);
            pstmt.setDouble(2, pbData.Sum);
            pstmt.setString(3, pbData.Currency);

            pstmt.executeUpdate();
            return "Success!";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
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
