package WebUI;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class WebUI {

    @RequestMapping("/")
    public String home() {
        return "Hello World";
    }
}
