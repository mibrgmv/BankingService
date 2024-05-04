package bankingservice.ui;

import bankingservice.bank.service.BankService;
import bankingservice.bank.service.ClientService;
import bankingservice.database.ClientDatabase;

import java.sql.SQLException;
import java.util.Scanner;

public class ConsoleUI {

    private Scanner scanner = new Scanner(System.in);
    private BankService bankService;
    private ClientService clientService;

    public ConsoleUI(BankService bankService, ClientService clientService) {
        this.bankService = bankService;
        this.clientService = clientService;
    }

    public void run() {
        while (true) {
            System.out.println("Select app mode (1-user; 2-Central Bank; 3-exit)");
            String mode = scanner.next();
            switch (mode) {
                case "1":
                    runUserMode();
                    break;
                case "2":
                    runCentralBankMode();
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Incorrect mode. Try again");
                    break;
            }
        }
    }

    private void runUserMode() {
        while (true) {
            System.out.println("Select option (1-login; 2-create account)");
            String mode = scanner.next();
            switch (mode) {
                case "1":
                    loginScenario();
                    break;
                case "2":
                    createAccountScenario();
                    break;
            }
        }
    }

    private void runCentralBankMode() {

    }

    private void loginScenario() {
        while (true) {
            System.out.println("Enter name and surname");
            String name = scanner.next();
            String surname = scanner.next();
            try {
                var client = ClientDatabase.findByNameAndSurname(name, surname);
                if (client == null) {
                    System.out.println("Cannot find client. Try again.");
                    return;
                } else {
                    System.out.println("Login successful");
                    loggedInScenario();
                }
            } catch (SQLException e) {

            }
        }
    }

    private void createAccountScenario() {

    }

    private void loggedInScenario() {

    }
}
