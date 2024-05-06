package bankingservice.bank.account;

public enum TransactionType {

    DEPOSIT("Депозит"),
    WITHDRAWAL("Снятие"),
    TRANSFER("Перевод"),
    RECEIVING("Получение"),
    INTEREST("Проценты");

    private final String name;

    TransactionType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
