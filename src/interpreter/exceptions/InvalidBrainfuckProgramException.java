package interpreter.exceptions;

import java.io.IOException;

public class InvalidBrainfuckProgramException extends IOException {
    public InvalidBrainfuckProgramException(String message) {
        super(message);
    }
}

