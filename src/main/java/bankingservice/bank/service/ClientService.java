package bankingservice.bank.service;

import bankingservice.bank.client.Client;
import bankingservice.database.ClientDatabase;

import java.sql.SQLException;
import java.time.LocalDate;

public class ClientService {

    public void registerClient(String name, String surname, LocalDate dateOfBirth) throws SQLException {
        ClientDatabase.add(name, surname, dateOfBirth.toString());
    }

    public Client findClientById(int id) throws SQLException {
        return ClientDatabase.findById(id);
    }
}
