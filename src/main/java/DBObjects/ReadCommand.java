package DBObjects;

import Interfaces.Validatable;

public class ReadCommand implements Validatable {
    private String command;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public boolean isValid() {
        return command != null && (command.equals("allm") || command.equals("m+") ||
                                   command.equals("m-") || command.equals("init") ||
                                   command.equals("bal"));
    }
}
