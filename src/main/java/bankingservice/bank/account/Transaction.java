package bankingservice.bank.account;

import java.time.LocalDate;

public record Transaction(int id, int accountId, int bankId, double amount, TransactionType transactionType, LocalDate date, boolean isUndo) {

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
