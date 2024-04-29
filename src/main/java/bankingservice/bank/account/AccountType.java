package bankingservice.bank.account;

public enum AccountType {

    SAVINGS("Депозитный"),
    DEBIT("Дебетовый"),
    CREDIT("Кредитный");

    private final String name;

    AccountType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

