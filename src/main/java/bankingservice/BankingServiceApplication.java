package bankingservice;

import bankingservice.bank.service.AccountService;
import bankingservice.bank.service.BankService;
import bankingservice.database.AccountDatabase;
import bankingservice.database.BankDatabase;
import bankingservice.database.ClientDatabase;
import bankingservice.database.TransactionDatabase;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

@SpringBootApplication
public class BankingServiceApplication {

	public static void main(String[] args) {
        try {
            var bank = BankDatabase.findById(1);
			System.out.println(bank.getName());

            var client = ClientDatabase.findById(1);
            System.out.println(client.getFirstName() + client.getLastName());

            var bankService = new BankService();
            bankService.setBank(bank);

            var account = AccountDatabase.findById(26, client, bank);
            var accountService = new AccountService(bank);
            accountService.deposit(account, 1000);

        } catch (SQLException e) {
			System.out.println(e.getMessage());
        }
    }
}
