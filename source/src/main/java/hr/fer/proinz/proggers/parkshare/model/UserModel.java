package hr.fer.proinz.proggers.parkshare.model;

import javax.persistence.*;

@Table(name = "usertable", indexes = {
        @Index(name = "User_username_key", columnList = "username", unique = true)
})
@Entity
public class UserModel {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "userid", nullable = false)
    private Integer id;

    @Column(name = "username", length = 35, nullable = false)
    private String name;

    @Column(name = "userfirstname", length = 35, nullable = false)
    private String firstName;

    @Column(name = "usersurname", length = 35, nullable = false)
    private String surname;

    @Column(name = "useremail", length = 50, nullable = false)
    private String email;

    @Column(name = "temppassword", nullable = false)
    private String tempPassword;

    @Column(name = "usertype", length = 15, nullable = false)
    private String type;

    @Column(name = "confirmed", nullable = false)
    private Boolean confirmed = false;

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTempPassword() {
        return tempPassword;
    }

    public void setTempPassword(String tempPassword) {
        this.tempPassword = tempPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String Email) {
        this.email = Email;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String Surname) {
        this.surname = Surname;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String FirstName) {
        this.firstName = FirstName;
    }

    public String getName() {
        return name;
    }

    public void setName(String Name) {
        this.name = Name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "Name = " + name + ", " +
                "FirstName = " + firstName + ", " +
                "Surname = " + surname + ", " +
                "Email = " + email + ", " +
                "tempPassword = " + tempPassword + ", " +
                "type = " + type + ", " +
                "confirmed = " + confirmed + ")";
    }

//    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private ParkingOwner parkingOwner;
//
//    public ParkingOwner getParkingOwner() {
//        return parkingOwner;
//    }
//
//    public void setParkingOwner(ParkingOwner parkingOwner) {
//        this.parkingOwner = parkingOwner;
//    }
//
//    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private Client client;
//
//    public Client getClient() {
//        return client;
//    }
//
//    public void setClient(Client client) {
//        this.client = client;
//    }
}