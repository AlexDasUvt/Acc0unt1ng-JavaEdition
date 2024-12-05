import TerminalUI.TerminalUI;
import API.APIController;
import GUI.GUIController;
import javafx.application.Application;
import org.springframework.boot.SpringApplication;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        boolean isTerminal = false;
        boolean isApiOnly = false;
        boolean isDebugMode = false;

        for (String arg : args) {
            switch (arg) {
                case "-terminalUI":
                    isTerminal = true;
                    break;
                case "-apiOnly":
                    isApiOnly = true;
                    break;
                case "-debug":
                    isDebugMode = true;
                    break;
            }
        }

        ExecutorService executor = Executors.newFixedThreadPool(2);

        if (isApiOnly) {
            SpringApplication.run(APIController.class, args);
        } else if (isTerminal) {
            TerminalUI terminalUI = new TerminalUI(isDebugMode);
            terminalUI.run();
        } else {
            executor.submit(() -> {
                SpringApplication.run(APIController.class, args);
            });

            executor.submit(() -> {
                Application.launch(GUIController.class, args);
            });
        }
        executor.shutdown();
    }
}
