package bankingservice.bank.service;

import bankingservice.bank.account.Account;
import bankingservice.bank.client.Client;
import bankingservice.database.AccountDatabase;
import bankingservice.database.ClientDatabase;

import java.sql.SQLException;
import java.util.List;

public class ClientService {

    public int registerClient(String name, String surname, String dateOfBirth) throws SQLException {
        return ClientDatabase.add(name, surname, dateOfBirth);
    }

    public Client findClientById(int id) throws SQLException {
        return ClientDatabase.findById(id);
    }

    public Client findClientByName(String firstName, String lastName) throws SQLException {
        return ClientDatabase.findByNameAndSurname(firstName, lastName);
    }

    public void setAddress(int id, String newAddress) throws SQLException {
        ClientDatabase.alterAddress(id, newAddress);
        updateSuspicious(id);
    }

    public void setPassport(int id, String newPassportNumber) throws SQLException {
        ClientDatabase.alterPassportNumber(id, newPassportNumber);
        updateSuspicious(id);
    }

    public boolean hasAddress(int id) throws SQLException {
        Client client = ClientDatabase.findById(id);
        return client.address() != null && !client.address().isEmpty();
    }

    public boolean hasPassport(int id) throws SQLException {
        Client client = ClientDatabase.findById(id);
        return client.passportNumber() != null && !client.passportNumber().isEmpty();
    }

    private void updateSuspicious(int id) throws SQLException {
        Client client = ClientDatabase.findById(id);
        if (client.address() != null && !client.address().isEmpty() && client.passportNumber() != null && !client.passportNumber().isEmpty()) {
            List<Account> accounts = AccountDatabase.getAccountsForClient(id);
            for (var account : accounts) {
                AccountDatabase.alterSuspicious(account.getId(), false);
            }
        }
    }
}
