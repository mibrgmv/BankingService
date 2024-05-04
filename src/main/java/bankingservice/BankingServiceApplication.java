package bankingservice;

import bankingservice.bank.service.BankService;
import bankingservice.bank.service.ClientService;
import bankingservice.ui.ConsoleUI;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankingServiceApplication {

	public static void main(String[] args) {
        new ConsoleUI(new BankService(), new ClientService(), "1234").run();
    }
}
