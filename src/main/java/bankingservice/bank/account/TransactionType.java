package bankingservice.bank.account;

public enum TransactionType {

    DEPOSIT("Депозит"),
    WITHDRAW("Снятие"),
    TRANSFER("Перевод"),
    RECEIVE("Получение"),
    INTEREST("Проценты");

    private final String name;

    TransactionType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
