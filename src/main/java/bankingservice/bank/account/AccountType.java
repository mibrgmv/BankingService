package bankingservice.bank.account;

public enum AccountType {

    SAVINGS("Savings"),
    DEBIT("Debit"),
    CREDIT("Credit");

    private final String name;

    AccountType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

