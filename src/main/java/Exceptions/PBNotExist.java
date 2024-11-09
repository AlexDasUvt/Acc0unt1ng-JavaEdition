package Exceptions;

public class PBNotExist extends RuntimeException {
    public PBNotExist() {
        super("Person-bank does not exist in database!");
    }
}
