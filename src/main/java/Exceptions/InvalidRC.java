package Exceptions;

public class InvalidRC extends RuntimeException {
    public InvalidRC(String code) {
      super("ReadCode '" + code + "' does not exist!");
    }
}
