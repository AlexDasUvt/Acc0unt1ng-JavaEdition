package API;

import DBObjects.RecordData;
import DBObjects.ResultData;
import DBScripts.Add;
import DBScripts.Read;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@RestController
@SpringBootApplication
public class APIController {

    @RequestMapping("/")
    public String home() {
        return "redirect:/help";
    }

    @RequestMapping("/help")
    public String help() {
        return """
                API - Help message:
                
                Go to /add for Add functions.
                Go to /read for Read functions.
                Go to /conf/spv to configure SPVs and account initialization function.
                Go to /conf/spv/read to read existing SPVs.
                """;
    }

    @GetMapping("/add")
    public String getAdd() {
        return """
                API - Add message:
                
                Send POST request with data on this address for program to Add record in database.
                The POST request should look like:
                
                     Key     |                       Value
                -------------|-------------------------------------------------------
                date         | *Date value*
                category     | *Category value*
                subCategory  | *Sub-Category value, if exist*
                personBank   | *Person-Bank pair*
                sum          | *Sum value. Positive for income, negative for expense*
                currency     | *Currency value*
                comment      | *Comment value, if exist*
                """;
    }

    @PostMapping("/add")
    public String postAdd(@RequestBody RecordData ob) {
        if (!ob.isValid()) {
            return "Invalid data Exception!";
        } else {
            Add add = new Add();
            add.normal(ob);
            return "Success!";
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
                """;
    }

    @PostMapping("/read")
    public String postRead(String ob) {
        if (CheckCommand(ob)) {
            ResultData rs = Read.ReadDB(ob);
            return rs.formatResultData();
        } else {
            return "Invalid command Exception!";
        }
    }

    private boolean CheckCommand(String ob) {
        return (ob.equalsIgnoreCase("allm") || ob.equalsIgnoreCase("m+")) ||
               ob.equalsIgnoreCase("m-");
    }
}

