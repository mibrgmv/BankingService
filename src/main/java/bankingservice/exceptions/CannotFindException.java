package bankingservice.exceptions;

public class CannotFindException extends Exception {
    public CannotFindException() {
    }

    public CannotFindException(String message) {
        super(message);
    }

    public CannotFindException(String message, Throwable cause) {
        super(message, cause);
    }
}
