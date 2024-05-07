package bankingservice.bank.service;

import bankingservice.bank.client.Client;
import bankingservice.database.ClientDatabase;

import java.sql.SQLException;

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
    }

    public void setPassport(int id, String newPassportNumber) throws SQLException {
        ClientDatabase.alterPassportNumber(id, newPassportNumber);
    }

    public boolean hasAddress(int id) throws SQLException {
        Client client = ClientDatabase.findById(id);
        return client.address() != null && !client.address().isEmpty();
    }

    public boolean hasPassport(int id) throws SQLException {
        Client client = ClientDatabase.findById(id);
        return client.passportNumber() != null && !client.passportNumber().isEmpty();
    }
}
