package bankingservice;

import bankingservice.bank.service.AccountService;
import bankingservice.bank.service.BankService;
import bankingservice.bank.service.ClientService;
import bankingservice.ui.ConsoleUI;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankingServiceApplication {

    public static void main(String[] args) {
        BankService bankService = new BankService();
        ClientService clientService = new ClientService();
        AccountService accountService = new AccountService();
        String centralBankPassword = "1234";
        new ConsoleUI(
                bankService,
                clientService,
                accountService,
                centralBankPassword
        ).run();
    }
}
