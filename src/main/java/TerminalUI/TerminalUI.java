package TerminalUI;

import DBObjects.RecordData;
import DBObjects.ResultData;
import DBScripts.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class TerminalUI {
    Scanner scan = new Scanner(System.in);
    SPVconf spvconf = new SPVconf();
    Add add = new Add();
    Delete delete = new Delete();

    public void run() {
        while (true) {
            System.out.println("""
                    TERMINAL UI\
                    
                    
                    Add.    Add field
                    Read.   Read
                    Del.    Delete field
                    Conf.   Configure special field values
                    Ndb.    New DataBase
                    Exit.   Exit Terminal UI
                    """);
            String input = scan.nextLine();

            if (input.equalsIgnoreCase("add")) {
                System.out.println("Enter data in next format (Date,Category,Sub-Category,Person-Bank,Sum,Currency,Comment):\n");
                String tInput = scan.nextLine();
                String[] lst = tInput.split(",");
                int sum = Integer.parseInt(lst[4]);
                RecordData rd = new RecordData(lst[0], lst[1], lst[2], lst[3], (double) sum, lst[5], lst[6]);

            } else if (input.equalsIgnoreCase("read")) {
                System.out.println("""
                        Enter mode in which read:
                        
                         allm - read all records
                         m+   - read all positive records
                         m-   - read all negative records
                        
                        """);
                String mode = scan.nextLine();
                ResultData rs = Read.ReadDB(mode);
                List<Map<String, Object>> Data = rs.getMap();

                if (Data.isEmpty()) {
                    System.out.println("No data found.");
                    break;
                }
                for (Map<String, Object> userMap : Data) {
                    // Print each key-value pair in the map
                    for (Map.Entry<String, Object> entry : userMap.entrySet()) {
                        System.out.print(entry.getKey() + ": " + entry.getValue() + "  ");
                    }
                    System.out.println(); // New line
                }

            } else if (input.equalsIgnoreCase("del")) {
                System.out.println("Enter ID to Delete: ");
                try {
                    int id = Integer.parseInt(scan.nextLine());
                    delete.DeleteOne(id);
                } catch (Exception e) {
                    System.out.println("Invalid ID");
                }

            } else if (input.equalsIgnoreCase("conf")) {
                PreSPVconf();

            } else if (input.equalsIgnoreCase("ndb")) {
                NewDB.createTable();
                System.out.println("Success!\n");

            } else if (input.equalsIgnoreCase("exit")) {
                break;
            }
        }
    }

    private void PreSPVconf() {
        boolean isSuccess = false;

        System.out.println("""
                What field values to edit?
                 catinc - income category
                 catexp - expense category
                 subcat - sub-category
                 curr - currency""");
        String path = scan.nextLine();
        System.out.println("Existing values:\n" + spvconf.ReadSPV(path));
        System.out.println("Enter new values: value1,value2,...\n");
        String line = scan.nextLine();
        System.out.println("Enter mode: a - append, r - replace\n");
        String mode = scan.nextLine();

        try {
            spvconf.WriteSPV(path, line, mode);
            isSuccess = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if (isSuccess) {
            System.out.println("Success!\n");
        }
    }
}
