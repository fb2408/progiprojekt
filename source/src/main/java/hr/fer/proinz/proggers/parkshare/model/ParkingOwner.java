package hr.fer.proinz.proggers.parkshare.model;

import javax.persistence.*;

@Table(name = "parkingowner")
@Entity
public class ParkingOwner {
    @Id
    @Column(name = "userid", nullable = false)
    private Integer id;

    @Column(name = "iban", length = 21)
    private String iban;

    @Column(name = "idpicture", length = 50)
    private String idPicture;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getIdPicture() {
        return idPicture;
    }

    public void setIdPicture(String idPicture) {
        this.idPicture = idPicture;
    }

    //    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parkingOwner", optional = false)
//    private User user;
//
//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }

//    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private Parking parking;
//
//    public Parking getParking() {
//        return parking;
//    }
//
//    public void setParking(Parking parking) {
//        this.parking = parking;
//    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "iban = " + iban + ", " +
                "idPicture = " + idPicture + ")";
    }
}