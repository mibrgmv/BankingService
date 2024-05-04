package bankingservice.bank.bank;

import bankingservice.database.BankDatabase;
import java.sql.SQLException;
import java.util.List;

public class CentralBank {

    private static CentralBank instance;

    private CentralBank() { }

    public static CentralBank getInstance() {
        if (instance == null) {
            instance = new CentralBank();
        }
        return instance;
    }

    public List<Bank> getBanks() throws SQLException {
        return BankDatabase.getBanks();
    }

    public void registerBank(String name, double debitInterestRate, double savingsInterestRate, double creditCommission, double creditLimit, double suspiciousAccountLimit) throws SQLException {
        if (name.isEmpty() || name.isBlank() || debitInterestRate < 0 || savingsInterestRate < 0 || creditCommission < 0 || creditLimit < 0 || suspiciousAccountLimit < 0) {
            throw new IllegalArgumentException("Blank or negative arguments");
        }
        BankDatabase.add(name, debitInterestRate, savingsInterestRate, creditCommission, creditLimit, suspiciousAccountLimit);
    }
}

