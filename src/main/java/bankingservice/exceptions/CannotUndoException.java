package bankingservice.exceptions;

public class CannotUndoException extends Exception {
    public CannotUndoException() {
    }

    public CannotUndoException(String message) {
        super(message);
    }

    public CannotUndoException(String message, Throwable cause) {
        super(message, cause);
    }
}
