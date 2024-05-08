package bankingservice.bank.account;

public enum TransactionType {

    DEPOSIT("Deposit"),
    WITHDRAWAL("Withdrawal"),
    TRANSFER("Transfer"),
    RECEIVING("Receiving"),
    INTEREST("Interest");

    private final String name;

    TransactionType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
