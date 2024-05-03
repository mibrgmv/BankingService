package bankingservice.bank.account;

import java.time.LocalDate;

public class Transaction {
    // todo
    // 1) сделать рекордом
    // 2) сделать операции односторонними (без destination acc/bank)
    // 3) если надо отменить операцию трансфера – отменяем две операции (отправки и получения)
    private int id;
    private int accountId;
    private int bankId;
    private double amount;
    private TransactionType transactionType;
    private LocalDate date;
    private boolean isUndo;

    public Transaction(int id, int accountId, int bankId, double amount, TransactionType transactionType, LocalDate date, boolean isUndo) {
        this.id = id;
        this.accountId = accountId;
        this.bankId = bankId;
        this.amount = amount;
        this.transactionType = transactionType;
        this.date = date;
        this.isUndo = isUndo;
    }

    public int getId() { return id; }
    public int getAccountId() { return accountId; }
    public int getBankId() { return bankId; }
    public double getAmount() { return amount; }
    public TransactionType getTransactionType() { return transactionType; }
    public LocalDate getDate() { return date; }
    public boolean isUndo() { return isUndo; }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", accountId=" + accountId +
                ", bankId=" + bankId +
                ", amount=" + amount +
                ", transactionType=" + transactionType +
                ", date=" + date +
                ", isUndo=" + isUndo +
                '}';
    }
}
