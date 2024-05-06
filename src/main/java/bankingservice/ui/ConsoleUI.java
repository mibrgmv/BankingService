package bankingservice.ui;

import bankingservice.bank.account.Account;
import bankingservice.bank.bank.CentralBank;
import bankingservice.bank.client.Client;
import bankingservice.bank.service.BankService;
import bankingservice.bank.service.ClientService;
import bankingservice.database.AccountDatabase;
import bankingservice.database.ClientDatabase;
import bankingservice.database.TransactionDatabase;
import bankingservice.exceptions.InsufficientFundsException;
import bankingservice.exceptions.SuspiciousLimitExceedingException;
import bankingservice.exceptions.WithdrawalBeforeEndDateException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
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
        System.out.print("first name: ");
        String name = scanner.nextLine();
        System.out.print("last name: ");
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
        do {
            System.out.println("available accounts:");
            List<Account> accounts;
            try {
                accounts = AccountDatabase.getAccountsForClient(userId);
            } catch (SQLException e) {
                System.out.println("error. could not get accounts\n");
                return;
            }
            int c = 0;
            for (var account : accounts) {
                System.out.print(++c + ". " + account.toString() + '\n');
            }
            System.out.print("select account: ");
            String s = scanner.nextLine();
            if (s.isEmpty()) {
                return;
            }
            int option;
            try {
                option = Integer.parseInt(s);
                if (option > 0 && option <= accounts.size()) {
                    accountManagement(accounts.get(option - 1).getId());
                } else {
                    System.out.println("option out of range.\n");
                }
            } catch (NumberFormatException e) {
                System.out.println("incorrect format.\n");
            }
        } while (true);
    }

    public void accountManagement(int accountId) {
        do {
            System.out.println("select operation: ");
            System.out.println("1-deposit");
            System.out.println("2-withdraw");
            System.out.println("3-transfer");
            System.out.println("4-view history");
            System.out.print("option: ");
            String option = scanner.nextLine();
            if (option.isEmpty()) {
                return;
            }
            switch (option) {
                case "1":
                    clientDepositScenario(accountId);
                    break;
                case "2":
                    clientWithdrawScenario(accountId);
                    break;
                case "3":
                    clientTransferScenario(accountId);
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
                    System.out.println("invalid input. try again...\n");
            }
        } while (true);
    }

    public void clientDepositScenario(int accountId) {
        do {
            System.out.print("enter amount to deposit: ");
            String input = scanner.nextLine();
            if (input.isEmpty()) {
                return;
            }
            try {
                int amount = Integer.parseInt(input);
                Account account = AccountDatabase.findById(accountId);
                Objects.requireNonNull(account).deposit(amount);
                System.out.println("deposit successful.\n");
                return;
            } catch (IllegalArgumentException e) {
                System.out.println("invalid format.\n");
            } catch (SQLException | NullPointerException e) {
                System.out.println("error. cannot retrieve account data.\n");
            }
        } while (true);
    }

    public void clientWithdrawScenario(int accountId) {
        do {
            System.out.print("enter amount to withdraw: ");
            String input = scanner.nextLine();
            if (input.isEmpty()) {
                return;
            }
            try {
                int amount = Integer.parseInt(input);
                Account account = AccountDatabase.findById(accountId);
                Objects.requireNonNull(account).withdraw(amount);
                System.out.println("withdrawal successful.\n");
                return;
            } catch (IllegalArgumentException e) {
                System.out.println("invalid format.\n");
            } catch (SQLException e) {
                System.out.println("internal withdrawal error.\n");
            } catch (NullPointerException e) {
                System.out.println("error. cannot retrieve account data.\n");
            } catch (SuspiciousLimitExceedingException e) {
                System.out.println("account suspicious. cannot withdraw above limit.\n");
            } catch (InsufficientFundsException e) {
                System.out.println("insufficient funds.\n");
            } catch (WithdrawalBeforeEndDateException e) {
                System.out.println("cannot withdraw from savings account before expiry date.\n ");
                return;
            }
        } while (true);
    }

    public void clientTransferScenario(int accountId) {
        String input;
        int amount, id;

        do {
            do {
                System.out.print("enter amount to transfer: ");
                input = scanner.nextLine();
                if (input.isEmpty()) {
                    return;
                }
                try {
                    amount = Integer.parseInt(input);
                    break;
                } catch (NullPointerException | NumberFormatException e) {
                    System.out.println("invalid amount.\n");
                }
            } while (true);

            do {
                System.out.print("enter recipient account id: ");
                input = scanner.nextLine();
                if (input.isEmpty()) {
                    return;
                }
                try {
                    id = Integer.parseInt(input);
                    break;
                } catch (NullPointerException | NumberFormatException e) {
                    System.out.println("invalid id.\n");
                }
            } while (true);

            try {
                Account account = AccountDatabase.findById(accountId);
                Objects.requireNonNull(account).transfer(id, amount);
                System.out.println("transfer successful.\n");
                return;
            } catch (SuspiciousLimitExceedingException e) {
                System.out.println("account suspicious. cannot transfer above limit.\n");
            } catch (SQLException e) {
                System.out.println("internal transfer error.\n");
            } catch (InsufficientFundsException e) {
                System.out.println("insufficient funds.\n");
            } catch (WithdrawalBeforeEndDateException e) {
                System.out.println("cannot transfer from savings account before expiry date.\n ");
                return;
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
                        System.out.println("incorrect data format.");
                    }
                    break;
                case "3":
                    try {
                        centralBank.addInterest();
                    } catch (SQLException e) {
                        System.out.println("an unexpected error occurred.");
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
