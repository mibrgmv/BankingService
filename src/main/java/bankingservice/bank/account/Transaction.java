package bankingservice.bank.account;

import java.time.LocalDate;

public class Transaction {

    private int transactionId;
    private TransactionType transactionType;
    private double amount;
    private Integer destinationAccountId;
    private LocalDate date;

    public Transaction(TransactionType transactionType, double amount, LocalDate date) {
        this.transactionId = 0;
        this.transactionType = transactionType;
        this.amount = amount;
        this.destinationAccountId = null;
        this.date = date;
    }

    public Transaction(TransactionType transactionType, double amount, int destinationAccountId, LocalDate date) {
        this.transactionId = 0;
        this.transactionType = transactionType;
        this.amount = amount;
        this.destinationAccountId = destinationAccountId;
        this.date = date;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public Integer getDestinationAccountId() {
        return destinationAccountId;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", transactionType=" + transactionType +
                ", amount=" + amount +
                ", destinationAccountId=" + destinationAccountId +
                ", date=" + date +
                '}';
    }
}
