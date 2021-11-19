package hr.fer.proinz.proggers.parkshare.dto;

import java.math.BigDecimal;

public class UserDTO {

    private Integer id;
    private String username;
    private String firstName;
    private String lastName;
    private String mail;
    private String password;
    private String confirmationPassword;
    private String role;
    private String iban;
    private BigDecimal walletBalance;

    private boolean confirmed;

    public UserDTO(Integer userid, String username, String firstName, String userSurname, String mail, String password, String role, String iban, BigDecimal walletBalance, boolean confirmed) {
        this.id = userid;
        this.username = username;
        this.firstName = firstName;
        this.lastName = userSurname;
        this.mail = mail;
        this.password = password;
        this.role = role;
        this.iban = iban;
        this.walletBalance = walletBalance;
        this.confirmed = confirmed;
    }

    public UserDTO() {

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getConfirmationPassword() {
        return confirmationPassword;
    }

    public void setConfirmationPassword(String confirmationPassword) {
        this.confirmationPassword = confirmationPassword;
    }

    public String getUserfirstname() {
        return firstName;
    }

    public void setUserfirstname(String userfirstname) {
        this.firstName = userfirstname;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public BigDecimal getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(BigDecimal walletBalance) {
        this.walletBalance = walletBalance;
    }

    public boolean isOwner() {
        return role.equals("owner");
    }

    public boolean isAdmin() {
        return role.equals("admin");
    }

    public boolean isClient() {
        return role.equals("client");
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", mail='" + mail + '\'' +
                ", password='" + password + '\'' +
                ", confirmationPassword='" + confirmationPassword + '\'' +
                ", role='" + role + '\'' +
                ", iban='" + iban + '\'' +
                ", walletBalance=" + walletBalance +
                ", confirmed=" + confirmed +
                '}';
    }
}
