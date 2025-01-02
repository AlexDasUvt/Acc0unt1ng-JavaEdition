package Settings;

public class GlobalSettings {
    public static boolean isDebugMode = false;
    public static boolean isTerminal = false;
    public static boolean isApiOnly = false;

    public static void EnableDebugMode() {
        isDebugMode = true;
    }

    public static void EnableTerminalMode() {
        isTerminal = true;
    }

    public static void EnableApiOnlyMode() {
        isApiOnly = true;
    }
}
