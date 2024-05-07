package bankingservice.exceptions;

public class AccountDeletionException extends Exception {
    public AccountDeletionException() {
    }

    public AccountDeletionException(String message) {
        super(message);
    }

    public AccountDeletionException(String message, Throwable cause) {
        super(message, cause);
    }
}
