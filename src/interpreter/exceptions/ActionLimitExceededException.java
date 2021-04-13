package interpreter.exceptions;

public class ActionLimitExceededException extends Exception {
    public ActionLimitExceededException(String message) {
        super(message);
    }
}
