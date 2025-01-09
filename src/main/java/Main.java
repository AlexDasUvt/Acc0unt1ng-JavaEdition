import GUI.StartController;
import Settings.GlobalSettings;
import TerminalUI.TerminalUI;
import API.APIController;
import javafx.application.Application;
import org.springframework.boot.SpringApplication;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        for (String arg : args) {
            switch (arg) {
                case "-terminalUI":
                    GlobalSettings.EnableTerminalMode();
                    break;
                case "-apiOnly":
                    GlobalSettings.EnableApiOnlyMode();
                    break;
                case "-debug":
                    GlobalSettings.EnableDebugMode();
                    break;
            }
        }

        if (GlobalSettings.isApiOnly) {
            SpringApplication.run(APIController.class, args);
        } else if (GlobalSettings.isTerminal) {
            TerminalUI terminalUI = new TerminalUI();
            terminalUI.run();
        } else {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(() -> SpringApplication.run(APIController.class, args));

            Application.launch(StartController.class, args);

            executor.shutdown();
        }
    }
}
