package hr.fer.proinz.proggers.parkshare.dto;

import hr.fer.proinz.proggers.parkshare.model.Client;
import hr.fer.proinz.proggers.parkshare.model.ClientReservation;
import hr.fer.proinz.proggers.parkshare.model.ParkingOwner;
import hr.fer.proinz.proggers.parkshare.model.UserModel;
import hr.fer.proinz.proggers.parkshare.repo.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public class UserDTO {

    private Integer userid;
    private String username;
    private String firstName;
    private String userSurname;
    private String userMail;
    private String temppassword;
    private String userType;
    private String iban;
    private BigDecimal walletBalance;

    private boolean confirmed;

    public UserDTO(Integer userid, String username, String firstName, String userSurname, String userMail, String temppassword, String userType, String iban, BigDecimal walletBalance, boolean confirmed) {
        this.userid = userid;
        this.username = username;
        this.firstName = firstName;
        this.userSurname = userSurname;
        this.userMail = userMail;
        this.temppassword = temppassword;
        this.userType = userType;
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

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserfirstname() {
        return firstName;
    }

    public void setUserfirstname(String userfirstname) {
        this.firstName = userfirstname;
    }

    public String getUserSurname() {
        return userSurname;
    }

    public void setUserSurname(String userSurname) {
        this.userSurname = userSurname;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public String getTemppassword() {
        return temppassword;
    }

    public void setTemppassword(String temppassword) {
        this.temppassword = temppassword;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
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
        return userType.equals("owner");
    }

    public boolean isAdmin() {
        return userType.equals("admin");
    }

    public boolean isClient() {
        return userType.equals("client");
    }
}
