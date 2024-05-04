package bankingservice.ui;

import bankingservice.bank.bank.CentralBank;
import bankingservice.bank.client.Client;
import bankingservice.bank.service.BankService;
import bankingservice.bank.service.ClientService;
import bankingservice.database.ClientDatabase;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

public class ConsoleUI {

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
            System.out.println("select app mode: (1-user; 2-central bank; enter-exit)");
            String mode = scanner.nextLine();
            if (mode.isEmpty()) {
                break;
            }
            switch (mode) {
                case "1":
                    runUserMode();
                    break;
                case "2":
                    runCentralBankMode();
                    break;
                default:
                    System.out.println("try again...");
            }
        }
    }

    private void runUserMode() {
        while (true) {
            System.out.println("options: [1-login; 2-create account; enter-exit]");
            String mode = scanner.nextLine();
            if (mode.isBlank()) {
                break;
            }
            switch (mode) {
                case "1":
                    loginScenario();
                    break;
                case "2":
                    createAccountScenario();
                    break;
                default:
                    System.out.println("invalid option. retry...");
            }
        }
    }

    private void runCentralBankMode() {
        System.out.println("enter password");
        String password = scanner.nextLine();
        if (Objects.equals(password, centralBankPassword)) {
            centralBankMenu();
        } else {
            System.out.println("incorrect password");
        }
    }

    public void centralBankMenu() {
        while (true) {
            System.out.println("1-display all banks");
            System.out.println("2-create a bank");
            System.out.println("enter-exit");
            String option = scanner.next();
            switch (option) {
                case "1":
                    try {
                        for (var bank : centralBank.getBanks()) {
                            System.out.println(bank);
                        }
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "2":
                    try {
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
                    } catch (SQLException | IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    } catch (InputMismatchException e) {
                        System.out.println("incorrect data format");
                    }
                    System.out.println();
                    break;
                default:
                    System.out.println("try again...");
            }
        }
    }

    private void loginScenario() {
        System.out.println("first name: ");
        String name = scanner.next();
        System.out.println("last name: ");
        String surname = scanner.next();
        Client client;
        try {
            client = ClientDatabase.findByNameAndSurname(name, surname);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (client == null) {
            System.out.println("cannot find client. try again.");
        } else {
            loggedInScenario();
        }
    }

    private void createAccountScenario() {
        while (true) {
            System.out.println("To create an account, enter your");
            System.out.print("first name: ");
            String name = scanner.next();
            System.out.print("last name: ");
            String surname = scanner.next();
            System.out.print("date of birth (yyyy-MM-dd): ");
            String dob = scanner.next();
            try {
                dob = String.valueOf(LocalDate.parse(dob, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            } catch (DateTimeParseException e) {
                System.out.println("incorrect date format. try again.");
                break;
            }
            try {
                ClientDatabase.add(name, surname, dob);
                loggedInScenario();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                break;
            }
        }
    }

    private void loggedInScenario() {
        System.out.println("successful login");
    }
}
