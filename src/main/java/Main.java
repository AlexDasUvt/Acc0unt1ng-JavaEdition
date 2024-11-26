import TerminalUI.TerminalUI;
import API.APIController;
import org.springframework.boot.SpringApplication;

public class Main {
    public static void main(String[] args) {
        TerminalUI terminalUI;
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
            terminalUI = new TerminalUI(isDebugMode);
            terminalUI.run();
        } else {
            SpringApplication.run(APIController.class, args);
        }
    }
}