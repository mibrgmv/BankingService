package bankingservice.exceptions;

public class SuspiciousLimitExceedingException extends Exception {
    public SuspiciousLimitExceedingException() {
    }

    public SuspiciousLimitExceedingException(String message) {
        super(message);
    }

    public SuspiciousLimitExceedingException(String message, Throwable cause) {
        super(message, cause);
    }
}
