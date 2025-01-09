package API;

import DBObjects.InitPBData;
import DBObjects.ReadCommand;
import DBObjects.RecordData;
import DBObjects.ResultData;
import DBScripts.Add;
import DBScripts.Read;
import Enums.ReadCode;
import DBScripts.SPVconf;
import Exceptions.InvalidRC;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import static DBScripts.Read.getReadCode;

@RestController
@SpringBootApplication
public class APIController { // TODO Implement Debuggable into APIController

    @RequestMapping("/")
    public RedirectView home() {
        return new RedirectView("/help");
    }

    @RequestMapping("/help")
    public String help() {
        return """
                API - Help message:
                
                Go to /add for Add functions.
                Go to /read for Read functions.
                Go to /conf/spv/initpb for account initialization function.
                """;
    }

    @RequestMapping("/add")
    public String add() {
        return """
                API - Add message:
                
                Go to /add/main for main record Add function.
                Go to /add/transfer for transfer record Add function.
                """;
    }

    @GetMapping("/add/main")
    public String getAddMain() {
        return """
                API - Add - Main message:
                
                Send POST request with data on this address for program to Add record in database.
                The POST request should look like:
                
                     Key     |                       Value
                -------------|-------------------------------------------------------
                date         | *Date value YYYY-MM-DD*
                category     | *Category value*
                subCategory  | *Sub-Category value, if exist*
                personBank   | *Person-Bank pair*
                sum          | *Sum value. Positive for income, negative for expense*
                currency     | *Currency value*
                comment      | *Comment value, if exist*
                """;
    }

    @PostMapping("/add/main")
    public String postAddMain(@RequestBody RecordData ob) {
        if (!ob.isValid()) {
            return "Invalid data Exception!";
        } else {
            Add add = new Add();
            try {
                add.normal(ob);
                return "Success!";
            } catch (Exception e) {
                return "Error: " + e.getMessage();
            }
        }
    }

    @GetMapping("/add/transfer")
    public String getAddTransfer() {
        return """
                API - Add - Transfer message:
                
                Send POST request with data on this address for program to Add record in database.
                The POST request should look like:
                
                     Key     |                       Value
                -------------|-------------------------------------------------------
                date         | *Date value YYYY-MM-DD*
                personBank   | *Person-Bank pair of sender*
                personBankTo | *Person-Bank pair of receiver*
                sum          | *Sum value. Positive for income, negative for expense*
                currency     | *Currency value*
                comment      | *Comment value, if exist*
                """;
    }

    @PostMapping("/add/transfer")
    public String postAddTransfer(@RequestBody RecordData ob) {
        if (!ob.isValidTransfer()) {
            return "Invalid data Exception!";
        } else {
            Add add = new Add();
            try {
                add.transfer(ob);
                return "Success!";
            } catch (Exception e) {
                return "Error: " + e.getMessage();
            }
        }
    }

    @GetMapping("/read")
    public String getRead() {
        return """
                API - Read message:
                
                Send POST request with data on this address for program to Read records from database.
                The POST request should look like:
                
                   Key  |        Value
                --------|-----------------------
                command | [From commands below]
                
                Commands available:
                 allm   - For all records in the database.
                 m+     - For all positive records in the database.
                 m-     - For all negative records in the database.
                 tran   - For all transfer records.
                 init   - For all initial records in the database.
                 bal    - Total balance on all accounts.
                """;
    }

    @PostMapping("/read")
    public String postRead(@RequestBody ReadCommand readCommand) {
        if (!readCommand.isValid()) {
            return "Invalid code Exception!";
        }

        ReadCode rc;
        try {
            rc = getReadCode(readCommand.getCommand());
        } catch (InvalidRC e) {
            return "Error: " + e.getMessage();
        }

        ResultData rs = Read.ReadDB(rc);
        return rs.formatResultData();
    }

    @PostMapping("/read/raw")
    public ResultData postReadRaw(@RequestBody ReadCommand readCommand) {
        if (readCommand.isValid()) {
            ReadCode rc = getReadCode(readCommand.getCommand());
            return Read.ReadDB(rc);
        }
        System.out.println("Invalid code Exception!");
        return null;
    }

    @GetMapping("/conf/spv/initpb")
    public String getInitPB() {
        return """
                API - Conf - SPV - InitPB message:
                
                Send POST request with data on this address for program Initialize new Person_bank account.
                The POST request should look like:
                
                   Key      |        Value
                ------------|-----------------------
                PersonBank  | *Person-bank value*
                Sum         | *Sum value*
                Currency    | *Currency value*
                """;
    }

    @PostMapping("/conf/spv/initpb")
    public String postInitPB(@RequestBody InitPBData ob) {
        SPVconf spv = new SPVconf();
        try {
            spv.InitPB(ob);
            return "Success!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}

