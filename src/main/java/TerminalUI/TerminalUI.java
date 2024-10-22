package TerminalUI;

import DBScripts.Add;
import DBScripts.NewDB;
import DBScripts.Read;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class TerminalUI {
    Scanner scan = new Scanner(System.in);

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

            if (input.equalsIgnoreCase("Add") || input.equalsIgnoreCase("add")) {
                System.out.println("Enter data in next format (Date,Category,Sub-Category,Person-Bank,Sum,Currency,Comment):\n");
                String tInput = scan.nextLine();
                Add.normal(tInput);
            } else if (input.equalsIgnoreCase("Read") || input.equalsIgnoreCase("read")) {
                System.out.println("""
                        Enter mode in which read:
                        
                         allm - read all records
                         m+   - read all positive records
                         m-   - read all negative records
                        
                        """);
                String mode = scan.nextLine();
                List<Map<String, Object>> Data = Read.ReadDB(mode);

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

            } else if (input.equalsIgnoreCase("Del") || input.equalsIgnoreCase("del")) {
                //TODO Delete function
            } else if (input.equalsIgnoreCase("Conf") || input.equalsIgnoreCase("conf")) {
                //TODO Special fields configuration
            } else if (input.equalsIgnoreCase("Ndb") || input.equalsIgnoreCase("ndb")) {
                NewDB.createTable();
                System.out.println("Success!\n");
            } else if (input.equalsIgnoreCase("Exit") || input.equalsIgnoreCase("exit")) {
                break;
            }
        }
    }
}
