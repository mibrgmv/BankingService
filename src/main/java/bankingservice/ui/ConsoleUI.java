package bankingservice.ui;

import bankingservice.bank.bank.CentralBank;
import bankingservice.bank.client.Client;
import bankingservice.bank.service.BankService;
import bankingservice.bank.service.ClientService;
import bankingservice.database.AccountDatabase;
import bankingservice.database.ClientDatabase;
import bankingservice.database.TransactionDatabase;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

public class ConsoleUI implements UI {

    private Scanner scanner = new Scanner(System.in);
    private BankService bankService;
    private ClientService clientService;
    private CentralBank centralBank = CentralBank.getInstance();
    private String centralBankPassword;

    public ConsoleUI(BankService bankService, ClientService clientService, String centralBankPassword) {
        this.bankService = bankService;
        this.clientService = clientService;
        this.centralBankPassword = centralBankPassword;
    }

    public void run() {
        while (true) {
            System.out.println("select app mode: (1-user; 2-central bank; 'enter'-exit)");
            String mode = scanner.nextLine();
            if (mode.isEmpty()) {
                return;
            }
            switch (mode) {
                case "1":
                    runUserMode();
                    break;
                case "2":
                    runCentralBankMode();
                    break;
                default:
                    System.out.println("try again...\n");
            }
        }
    }

    private void runUserMode() {
        while (true) {
            System.out.println("--client options-press 'enter' to return--");
            System.out.println("1-login");
            System.out.println("2-create account");
            String mode = scanner.nextLine();
            if (mode.isEmpty()) {
                return;
            }
            switch (mode) {
                case "1":
                    loginScenario();
                    break;
                case "2":
                    createAccountScenario();
                    break;
                default:
                    System.out.println("invalid option. retry...\n");
            }
        }
    }

    private void loginScenario() {
        System.out.println("first name: ");
        String name = scanner.nextLine();
        System.out.println("last name: ");
        String surname = scanner.nextLine();
        Client client;
        try {
            client = ClientDatabase.findByNameAndSurname(name, surname);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (client != null) {
            userMenu(client.id());
        } else {
            System.out.println("cannot find client.\n");
        }
    }

    private void createAccountScenario() {
        System.out.println("to create an account, enter your");
        String name, surname, dob;

        do {
            System.out.print("first name: ");
            name = scanner.nextLine();
            if (name.isEmpty()) {
                return;
            }
            if (!name.matches("[A-Z][a-z]+")) {
                System.out.println("incorrect name format.\n");;
            }
        } while (!name.matches("[A-Z][a-z]+"));

        do {
            System.out.print("last name: ");
            surname = scanner.nextLine();
            if (surname.isEmpty()) {
                return;
            }
            if (!surname.matches("[A-Z][a-z]+")) {
                System.out.println("incorrect name format.\n");;
            }
        } while (!surname.matches("[A-Z][a-z]+"));

        do {
            System.out.print("date of birth (yyyy-MM-dd): ");
            dob = scanner.nextLine();
            if (dob.isEmpty()) {
                return;
            }
            try {
                dob = String.valueOf(LocalDate.parse(dob, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                break;
            } catch (DateTimeParseException | IllegalArgumentException e) {
                System.out.println("incorrect date format.\n");
            }
        } while (true);

        try {
            int id = ClientDatabase.add(name, surname, dob);
            userMenu(id);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void userMenu(int userId) {
        Client user;
        try {
            user = ClientDatabase.findById(userId);
            System.out.printf("\nwelcome %s %s!\n", user.firstName(), user.lastName());
        } catch (SQLException e) {
            System.out.println("could not access client profile.\n");
            return;
        }
        while (true) {
            try {
                System.out.println("available accounts:");
                var accounts = AccountDatabase.getAccountsForClient(userId);
                int c = 0;
                for (var account : accounts) {
                    System.out.print(++c + ". " + account.toString() + '\n');
                }
                System.out.println("select account: ");
                String s = scanner.nextLine();
                if (s.isEmpty()) {
                    return;
                }
                int option = Integer.parseInt(s);
                if (option > 0 && option <= accounts.size()) {
                    accountManagement(accounts.get(c-1).getId());
                }
            } catch (InputMismatchException  | NumberFormatException ignored) {
                System.out.println("invalid input.\n");
            } catch (SQLException e) {
                System.out.println("cannot access accounts.\n");
            }
        }
    }

    public void accountManagement(int accountId) {
        System.out.println("select operation: ");
        System.out.println("1-deposit");
        System.out.println("2-withdraw");
        System.out.println("3-transfer");
        System.out.println("4-view history");
        do {
            System.out.println("option: ");
            String option = scanner.nextLine();
            if (option.isEmpty()) {
                return;
            }
            switch (option) {
                case "1":
                    System.out.println("select amount for deposit: ");
                    break;
                case "4":
                    try {
                        var transactions = TransactionDatabase.getTransactionsForClient(accountId);
                        int c = 0;
                        for (var transaction : transactions) {
                            System.out.print(++c + ". " + transaction + '\n');
                        }
                    } catch (SQLException e) {
                        System.out.println("error loading transaction history.\n");
                    }
                    break;
                default:
                    System.out.println("invalid input.\n");
            }
        } while (true);
    }

    private void runCentralBankMode() {
        System.out.print("enter password: ");
        String password = scanner.nextLine();
        if (Objects.equals(password, centralBankPassword)) {
            centralBankMenu();
        } else {
            System.out.println("incorrect password\n");
        }
    }

    public void centralBankMenu() {
        while (true) {
            System.out.println("--central bank options-press 'enter' to return--");
            System.out.println("1-display all banks");
            System.out.println("2-create a bank");
            System.out.println("3-add interest to all accounts");
            String option = scanner.nextLine();
            if (option.isEmpty()) {
                return;
            }
            switch (option) {
                case "1":
                    try {
                        var banks = centralBank.getBanks();
                        System.out.println("BANKS:");
                        for (var bank : banks) {
                            System.out.println(bank);
                        }
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "2":
                    try {
                        bankCreationScenario();
                    } catch (SQLException | IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    } catch (InputMismatchException e) {
                        System.out.println("incorrect data format");
                    }
                    break;
                case "3":
                    try {
                        centralBank.addInterest();
                    } catch (SQLException e) {
                        System.out.println("an unexpected error occurred");
                    }
                    break;
                default:
                    System.out.println("try again...\n");
            }
        }
    }

    private void bankCreationScenario() throws SQLException {
        System.out.print("enter name: ");
        String name = scanner.nextLine();
        System.out.print("enter debit interest rate: ");
        double debitInterestRate = scanner.nextDouble();
        System.out.print("enter savings interest rate ");
        double savingsInterestRate = scanner.nextDouble();
        System.out.print("enter credit commission: ");
        double creditCommission = scanner.nextDouble();
        System.out.print("enter credit limit: ");
        double creditLimit = scanner.nextDouble();
        System.out.print("enter suspicious account limit: ");
        double suspiciousAccountLimit = scanner.nextDouble();
        centralBank.registerBank(name, debitInterestRate, savingsInterestRate, creditCommission, creditLimit, suspiciousAccountLimit);
    }
}
