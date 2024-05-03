package bankingservice.bank.client;

import java.time.LocalDate;

public record Client(int id, String firstName, String lastName, LocalDate dateOfBirth, String address, String passportNumber) {}
