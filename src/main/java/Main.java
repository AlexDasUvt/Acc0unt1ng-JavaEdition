import TerminalUI.TerminalUI;
import API.APIController;
import org.springframework.boot.SpringApplication;

public class Main {
    public static void main(String[] args) {
        boolean isTerminal = false;
        boolean isDebugMode = false;
        for (String arg : args) {
            if (arg.equals("-terminalUI")) {
                isTerminal = true;
            } else if (arg.equals("-debug")) {
                isDebugMode = true;
            }
        }
        if (isTerminal) {
            TerminalUI terminal = new TerminalUI(isDebugMode);
            terminal.run();
        } else {
            SpringApplication.run(APIController.class, args);
        }
    }
}