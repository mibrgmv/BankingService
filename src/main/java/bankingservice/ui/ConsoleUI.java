package bankingservice.ui;

import bankingservice.bank.account.Account;
import bankingservice.bank.account.AccountType;
import bankingservice.bank.bank.Bank;
import bankingservice.bank.bank.CentralBank;
import bankingservice.bank.client.Client;
import bankingservice.bank.service.AccountService;
import bankingservice.bank.service.BankService;
import bankingservice.bank.service.ClientService;
import bankingservice.database.AccountDatabase;
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
    private AccountService accountService;
    private CentralBank centralBank = CentralBank.getInstance();
    private String centralBankPassword;
    private final String firstNameRegex = "[A-Z][a-z]+";
    private final String lastNameRegex = "[A-Za-z ,.'-]+";
    private final String addressRegex = "([A-Z][a-z]+ )+(St|Rd|Ave|Blvd|Way|La|Dr), [0-9]+[a-z]*";
    private final String passportRegex = "[0-9]{4} [0-9]{6}";

    public ConsoleUI(BankService bankService, ClientService clientService, String centralBankPassword) {
        this.bankService = bankService;
        this.clientService = clientService;
        this.centralBankPassword = centralBankPassword;
    }

    public void run() {
        while (true) {
            System.out.println("--select app mode");
            System.out.println("1-user");
            System.out.println("2-central bank");
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
            System.out.println("\n--client options--");
            System.out.println("1-login");
            System.out.println("2-register");
            String mode = scanner.nextLine();
            if (mode.isEmpty()) {
                return;
            }
            switch (mode) {
                case "1":
                    loginScenario();
                    break;
                case "2":
                    registerClientScenario();
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
            client = clientService.findClientByName(name, surname);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (client != null) {
            userMenu(client.id());
        } else {
            System.out.println("cannot find client.");
        }
    }

    private void registerClientScenario() {
        System.out.println("\nto create an account, enter your");
        String name, surname, dob;

        do {
            System.out.print("first name: ");
            name = scanner.nextLine();
            if (name.isEmpty()) {
                return;
            }
            if (!name.matches(firstNameRegex)) {
                System.out.println("incorrect first name format.\n");;
            }
        } while (!name.matches(firstNameRegex));

        do {
            System.out.print("last name: ");
            surname = scanner.nextLine();
            if (surname.isEmpty()) {
                return;
            }
            if (!surname.matches(lastNameRegex)) {
                System.out.println("incorrect last name format.\n");;
            }
        } while (!surname.matches(lastNameRegex));

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
            int id = clientService.registerClient(name, surname, dob);
            userMenu(id);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void userMenu(int userId) {
        Client user;
        try {
            user = clientService.findClientById(userId);
            System.out.printf("\nwelcome %s %s!", user.firstName(), user.lastName());
        } catch (SQLException e) {
            System.out.println("could not access client profile.\n");
            return;
        }

        while (true) {
            System.out.println("\n--options--");
            System.out.println("1-set address");
            System.out.println("2-set passport number");
            System.out.println("3-browse accounts");
            System.out.println("4-open new account");
            String option = scanner.nextLine();
            if (option.isEmpty()) {
                return;
            }
            switch (option) {
                case "1":
                    try {
                        if (!clientService.hasAddress(userId)) {
                            offerToSetAddress(userId);
                        } else {
                            System.out.println("address is already set.");
                        }
                    } catch (SQLException e) {
                        System.out.println("an error occurred while address information.\n");
                    }
                    break;
                case "2":
                    try {
                        if (!clientService.hasPassport(userId)) {
                            offerToSetPassport(userId);
                        } else {
                            System.out.println("passport number is already set.");
                        }
                    } catch (SQLException e) {
                        System.out.println("an error occurred while passport information.\n");
                    }
                    break;
                case "3":
                    browseAccounts(userId);
                    break;
                case "4":
                    openAccountScenario(userId);
                    break;
                default:
                    System.out.println("invalid option. retry...\n");
            }
        }
    }

    public void offerToSetAddress(int userId) {
        String address;
        System.out.println("your address is not set. you can set it now or press 'enter' to do it later.");
        do {
            System.out.print("address (ex.: Mira St, 49a): ");
            address = scanner.nextLine();
            if (address.isEmpty()) {
                return;
            }
            if (!address.matches(addressRegex)) {
                System.out.println("incorrect address format.\n");
            }
        } while (!address.matches(addressRegex));

        try {
            clientService.setAddress(userId, address);
        } catch (SQLException e) {
            System.out.println("an error occurred while trying to set an address.\n");
        }
    }

    public void offerToSetPassport(int userId) {
        String passport;
        System.out.println("your passport number is not set. you can set it now or press 'enter' to do it later.");
        do {
            System.out.print("passport number (ex.: 1234 567890): ");
            passport = scanner.nextLine();
            if (passport.isEmpty()) {
                return;
            }
            if (!passport.matches(passportRegex)) {
                System.out.println("incorrect passport number format.\n");
            }
        } while (!passport.matches(passportRegex));

        try {
            clientService.setPassport(userId, passport);
        } catch (SQLException e) {
            System.out.println("an error occurred while trying to set a passport number.\n");
        }
    }

    public void browseAccounts(int userId) {
        do {
            List<Account> accounts;
            try {
                accounts = accountService.getAccountsForClient(userId);
            } catch (SQLException e) {
                System.out.println("error. could not get accounts\n");
                return;
            }
            if (accounts.size() == 0) {
                System.out.println("nothing here.");
                return;
            }
            System.out.println("available accounts:");
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
                    System.out.println("option out of range.");
                }
            } catch (NumberFormatException e) {
                System.out.println("incorrect format.");
            }
        } while (true);
    }

    public void openAccountScenario(int userId) {
        Client client;
        List <Bank> banks;
        try {
            client = clientService.findClientById(userId);
            banks = centralBank.getBanks();
            System.out.println("to create an account, select a bank and account type.");
            var bank = bankSelector(banks);
            if (bank == null) {
                return;
            }
            var type = accountTypeSelector();
            if (type == null) {
                return;
            }
            bankService.setBank(bank);
            if (type == AccountType.SAVINGS) {
                int duration = durationSelector();
                if (duration == -1) {
                    return;
                }
                bankService.openAccount(client, type, duration);
                System.out.println("account successfully opened.");
                return;
            }
            bankService.openAccount(client, type);
            System.out.println("account successfully opened.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Bank bankSelector(List <Bank> banks) {
        int c = 0;
        for (var bank : banks) {
            System.out.print(++c + ". " + bank.toString() + '\n');
        }
        do {
            System.out.print("bank: ");
            String s = scanner.nextLine();
            if (s.isEmpty()) {
                return null;
            }
            int option;
            try {
                option = Integer.parseInt(s);
                if (option > 0 && option <= banks.size()) {
                    return banks.get(option-1);
                } else {
                    System.out.println("option out of range.\n");
                }
            } catch (NumberFormatException e) {
                System.out.println("incorrect format.\n");
            }
        } while (true);
    }

    public AccountType accountTypeSelector() {
        int c = 0;
        for (var accountType : AccountType.values()) {
            System.out.print(++c + ". " + accountType.getName() + '\n');
        }
        do {
            System.out.print("account type: ");
            String s = scanner.nextLine();
            if (s.isEmpty()) {
                return null;
            }
            int option;
            try {
                option = Integer.parseInt(s);
                if (option > 0 && option <= AccountType.values().length) {
                    return AccountType.values()[option-1];
                } else {
                    System.out.println("option out of range.\n");
                }
            } catch (NumberFormatException e) {
                System.out.println("incorrect format.\n");
            }
        } while (true);
    }

    public int durationSelector() {
        System.out.println("for a savings account, please specify its duration (years)");
        do {
            System.out.print("duration: ");
            String s = scanner.nextLine();
            if (s.isEmpty()) {
                return -1;
            }
            int option;
            try {
                option = Integer.parseInt(s);
                if (option > 0 && option <= 999) {
                    return option;
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
                Account account = accountService.findAccountById(accountId);
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
                Account account = accountService.findAccountById(accountId);
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
                Account account = accountService.findAccountById(accountId);
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
            System.out.println("--central bank options--");
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
