package bankingservice.bank.client;

import java.time.LocalDate;

public class Client {

    private int clientId;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;
    private String passportNumber;

    public Client(int clientId, String firstName, String lastName, LocalDate dateOfBirth) {
        this.clientId = clientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }

    public int getClientId() {
        return clientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public boolean hasAddress() {
        return address != null && !address.isEmpty();
    }

    public boolean hasPassport() {
        return passportNumber != null && !passportNumber.isEmpty();
    }
}
