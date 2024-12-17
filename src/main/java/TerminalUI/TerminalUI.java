package TerminalUI;

import DBObjects.InitPBData;
import DBObjects.RecordData;
import DBObjects.ResultData;
import DBScripts.*;
import Enums.ReadCode;
import Exceptions.InvalidRC;
import Interfaces.Debuggable;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static DBScripts.Read.getReadCode;

public class TerminalUI implements Debuggable {
    private boolean isDebugMode = false;
    private Scanner scan = new Scanner(System.in);
    private SPVconf spvconf;
    private Add add;
    private Delete delete;

    public TerminalUI(boolean isDebugMode) {
        this.isDebugMode = isDebugMode;
        spvconf = new SPVconf(isDebugMode);
        add = new Add(isDebugMode);
        delete = new Delete(isDebugMode);
    }

    public void run() {
        while (true) {
            Debug("Inside menu loop");

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
                PreAdd();
            } else if (input.equalsIgnoreCase("read")) {
                Debug("Inside Read");
                ReadCode rc;
                System.out.println("""
                        Enter mode in which read:
                        
                         allm - read all records
                         m+   - read all positive records
                         m-   - read all negative records
                         tran - read all transfer records
                         init - read all initial records
                         bal  - sum remaining balance on all accounts
                        
                        """);
                String mode = scan.nextLine();
                try {
                    rc = getReadCode(mode);
                    Debug("ReadCode: " + rc.toString());
                } catch (InvalidRC e) {
                    System.out.println(e.getMessage());
                    continue;
                }

                ResultData rs = Read.ReadDB(rc);
                List<Map<String, Object>> Data = rs.getMap();

                if (Data.isEmpty()) {
                    System.out.println("No data found.");
                }
                for (Map<String, Object> userMap : Data) {
                    // Print each key-value pair in the map
                    for (Map.Entry<String, Object> entry : userMap.entrySet()) {
                        System.out.print(entry.getKey() + ": " + entry.getValue() + "  ");
                    }
                    System.out.println(); // New line
                }

            } else if (input.equalsIgnoreCase("del")) {
                Debug("Inside Delete");
                System.out.println("Enter ID to Delete: ");
                try {
                    int id = Integer.parseInt(scan.nextLine());
                    delete.DeleteOne(id);
                } catch (Exception e) {
                    System.out.println("Invalid ID");
                }

            } else if (input.equalsIgnoreCase("conf")) {
                System.out.println("Enter configuration mode\n initpb\n spv\n");
                String mode = scan.nextLine();
                if (mode.equalsIgnoreCase("spv")) {
                    PreSPVconf();
                } else if (mode.equalsIgnoreCase("inirpb")) {
                    PreInitPB();
                }

            } else if (input.equalsIgnoreCase("ndb")) {
                Debug("Inside NewDB");
                System.out.println("WARNING! DB will be rewritten! Proceed?\n");
                String confirmation = scan.nextLine();
                if (confirmation.equalsIgnoreCase("y") || confirmation.equalsIgnoreCase("yes")) {
                    NewDB.createTable();
                    System.out.println("Success!\n");
                } else {
                    System.out.println("Aborting!\n");
                }

            } else if (input.equalsIgnoreCase("exit")) {
                break;
            } else {
                System.out.println("Invalid input\n");
            }
        }
    }

    private void PreAdd() {
        Debug("Inside PreAdd");
        System.out.println("What record to add? main/transfer?\n");
        String input = scan.nextLine();
        try {
            if (input.equalsIgnoreCase("main")) {
                Debug("Inside PreAdd - main");
                System.out.println("Enter data in next format (Date,Category,Sub-Category,Person-Bank,Sum,Currency,Comment):\n");
                String tInput = scan.nextLine();

                String[] lst = tInput.split(",");
                int sum = Integer.parseInt(lst[4]);
                RecordData rd = new RecordData(lst[0], lst[1], lst[2], lst[3], null, (double) sum, lst[5], lst[6]);
                add.normal(rd);

            } else if (input.equalsIgnoreCase("transfer")) {
                Debug("Inside PreAdd - transfer");
                System.out.println("Enter data in next format (Date,Person-Bank Sender,Person-Bank Receiver,Sum,Currency,Comment):\n");
                String tInput = scan.nextLine();

                String[] lst = tInput.split(",");
                int sum = Integer.parseInt(lst[3]);
                RecordData rd = new RecordData(lst[0], null, null, lst[1], lst[2], (double) sum, lst[4], lst[5]);
                add.transfer(rd);

            } else {
                System.out.println("Invalid input\n");
            }
        } catch (RuntimeException e) {
            System.out.println("Error!: " + e.getMessage());
        }
    }

    private void PreInitPB() {
        Debug("Inside PreInitPB");
        System.out.println("Enter new account in next format: Person_bank,sum,currency");
        String line = scan.nextLine();
        InitPBData init = new InitPBData(line);
        try {
            spvconf.InitPB(init);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private void PreSPVconf() {
        Debug("Inside PreSPVconf");
        System.out.println("""
                What field values to edit?
                 cat - category
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
            System.out.println("Success!\n");
        } catch (Exception e) {
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
