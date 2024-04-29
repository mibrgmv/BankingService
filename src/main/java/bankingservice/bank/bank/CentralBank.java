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
        BankDatabase.add(name, debitInterestRate, savingsInterestRate, creditCommission, creditLimit, suspiciousAccountLimit);
    }
}

