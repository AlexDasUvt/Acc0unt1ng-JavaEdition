import TerminalUI.TerminalUI;
import API.APIController;
import org.springframework.boot.SpringApplication;

public class Main {
    public static void main(String[] args) {
        boolean isTerminal = false;
        for (String arg : args) {
            if (arg.equals("-terminalUI")) {
                isTerminal = true;
            }
        }
        if (isTerminal) {
            TerminalUI terminal = new TerminalUI();
            terminal.run();
        } else {
            SpringApplication.run(APIController.class, args);
        }
    }
}