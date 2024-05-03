package bankingservice;

import bankingservice.bank.account.Account;
import bankingservice.bank.bank.Bank;
import bankingservice.bank.service.BankService;
import bankingservice.database.AccountDatabase;
import bankingservice.database.BankDatabase;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

@SpringBootApplication
public class BankingServiceApplication {

	public static void main(String[] args) { //
        try {
            Bank b = BankDatabase.findById(1);
            BankService bs = new BankService();
            bs.setBank(b);

            Account a = AccountDatabase.findById(26);
            a.deposit(1000);
            System.out.println(a);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
