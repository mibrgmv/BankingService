package bankingservice.exceptions;

public class WithdrawalBeforeEndDateException extends Exception {
    public WithdrawalBeforeEndDateException() {
    }

    public WithdrawalBeforeEndDateException(String message) {
        super(message);
    }

    public WithdrawalBeforeEndDateException(String message, Throwable cause) {
        super(message, cause);
    }
}
