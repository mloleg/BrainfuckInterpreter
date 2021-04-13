package interpreter.exceptions;

import java.io.IOException;

public class InvalidInputArgumentsException extends IOException {
    public InvalidInputArgumentsException(String message) {
        super(message);
    }
}