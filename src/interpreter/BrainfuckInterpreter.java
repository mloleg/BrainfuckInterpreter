package interpreter;
import interpreter.exceptions.ActionLimitExceededException;
import interpreter.exceptions.BrainfuckMemoryArrayIndexOutOfBoundsException;
import interpreter.exceptions.InvalidBrainfuckProgramException;
import interpreter.exceptions.InvalidInputArgumentsException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class BrainfuckInterpreter {

    public static void main(String[] args) {
        try {
            checkArgs(args);
            String program = args[0];
            String input = args[1];

            int actionsLimit = -1;
            int memoryLimit = -1;

            try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
                actionsLimit = readNonNegativeNumber(in, "Want to limit the number of actions? y/n",
                        "Enter an integer - the number of actions");
                memoryLimit = readNonNegativeNumber(in, "Want to limit the memory array ? y/n",
                        "Enter an integer - the number of elements in the memory array. Default size - 30000 bytes");
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (actionsLimit == -1) {
                actionsLimit = Integer.MAX_VALUE;
            }

            if (memoryLimit == -1) {
                memoryLimit = 30000;
            }

            int inputPtr = 0, memoryPtr = 0, programPtr;
            byte[] memory = new byte[memoryLimit];

            for (programPtr = 0; programPtr < program.length() && programPtr <= actionsLimit; programPtr++) {
                switch (program.charAt(programPtr)) {
                    case '>' -> {
                        if (memoryPtr == memoryLimit - 1) {
                            throw new BrainfuckMemoryArrayIndexOutOfBoundsException("Out of bounds of memory array");
                        } else {
                            memoryPtr++;
                        }
                    }
                    case '<' -> {
                        if (memoryPtr == 0) {
                            throw new BrainfuckMemoryArrayIndexOutOfBoundsException("Jump on a negative index");
                        } else {
                            memoryPtr--;
                        }
                    }
                    case '+' -> memory[memoryPtr]++;
                    case '-' -> memory[memoryPtr]--;
                    case '.' -> System.out.print((char) memory[memoryPtr]);
                    case ',' -> {
                        if (inputPtr == input.length()) {
                            throw new Error();
                        }
                        memory[memoryPtr] = (byte) input.charAt(inputPtr);
                        inputPtr++;
                    }
                    case '[' -> {
                        if (memory[memoryPtr] == 0) {
                            int balance = 1;
                            while (balance != 0) {
                                programPtr++;
                                if (program.charAt(programPtr) == '[') {
                                    balance++;
                                } else if (program.charAt(programPtr) == ']') {
                                    balance--;
                                }
                            }
                            programPtr++;
                        }
                    }
                    case ']' -> {
                        if (memory[memoryPtr] != 0) {
                            int balance = 1;
                            while (balance != 0) {
                                programPtr--;
                                if (program.charAt(programPtr) == ']')
                                    balance++;
                                else if (program.charAt(programPtr) == '[')
                                    balance--;
                            }
                        }
                    }
                    default -> throw new InvalidBrainfuckProgramException("Unknown character at position " + programPtr);
                }
            }

            if (programPtr > actionsLimit) {
                throw new ActionLimitExceededException("Action limit exceeded\n");
            }

        } catch (InvalidBrainfuckProgramException e) {
            System.err.println("Exceptions.InvalidBrainfuckProgramException. " + e.getMessage());
        } catch (InvalidInputArgumentsException e) {
            System.err.println("Exceptions.InvalidInputArgumentsException. " + e.getMessage());
        } catch (ActionLimitExceededException e) {
            System.err.println("Exceptions.ActionLimitExceededException. " + e.getMessage());
        }
    }

    private static void checkArgs(String[] args) throws InvalidInputArgumentsException {
        if (args == null || args.length != 2 || args[0] == null || args[1] == null) {
            StringBuilder message = new StringBuilder();
            if (args == null) {
                message.append("Null input arguments ");
            } else {
                if (args.length != 2) {
                    message.append("The number of input parameters must be equal to two ");
                } else {
                    if (args[0] == null) {
                        message.append("Program is null ");
                    }
                    if (args[1] == null) {
                        message.append("Input is null ");
                    }
                }
            }
            throw new InvalidInputArgumentsException(message.toString());
        }
    }

    private static int readNonNegativeNumber(BufferedReader in, String question, String info) throws IOException {
        int result = -1;
        System.out.println(question);
        String str = in.readLine();

        while (!str.equals("y") && !str.equals("n")) {
            System.out.println("Please y or n");
            str = in.readLine().replaceAll("\\p{javaSpaceChar}", "");
        }

        if (str.equals("y")) {
            System.out.println(info);
            String inputNumber = in.readLine().replaceAll("\\p{javaSpaceChar}", "");

            while (!inputNumber.matches("[+]?\\d+")) {
                System.out.println("Please enter a non-negative number");
                inputNumber = in.readLine().replaceAll("\\p{javaSpaceChar}", "");
            }

            result = Integer.parseInt(inputNumber);
        }

        return result;
    }
}

// "Hello world!" tests
// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++.+++++++++++++++++++++++++++++.+++++++..+++.-------------------------------------------------------------------------------.+++++++++++++++++++++++++++++++++++++++++++++++++++++++.++++++++++++++++++++++++.+++.------.--------.-------------------------------------------------------------------.-----------------------.
// ++++++++++[>+++++++>++++++++++>+++>+<<<<-]>++.>+.+++++++..+++.>++.<<+++++++++++++++.>.+++.------.--------.>+.>.