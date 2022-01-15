package hr.fer.proinz.proggers.parkshare.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Table(name = "parking")
@Entity
public class Parking {
    @Id
    @Column(name = "userid", nullable = false)
    private Integer id;

    @Column(name = "parkingname", nullable = false, length = 50)
    private String parkingName;

    @Column(name = "parkingphoto", length = 50)
    private String parkingPhoto;

    @Column(name = "hourlyprice", precision = 131089)
    private BigDecimal hourlyPrice;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "entrancepointx", precision = 131089)
    private BigDecimal entrancepointx;

    @Column(name = "entrancepointy", precision = 131089)
    private BigDecimal entrancepointy;

    public String getDescription() { return description; }

    public void setDescription(String description) {this.description = description; }

    public BigDecimal getEntrancepointx() {return entrancepointx;}

    public void setEntrancepointx(BigDecimal entrancepointx) { this.entrancepointx = entrancepointx;}

    public BigDecimal getEntrancepointy() {return entrancepointy; }

    public void setEntrancepointy(BigDecimal entrancepointy) { this.entrancepointy = entrancepointy;}

    public String getParkingPhoto() {
        return parkingPhoto;
    }

    public void setParkingPhoto(String parkingPhoto) {
        this.parkingPhoto = parkingPhoto;
    }

    public BigDecimal getHourlyPrice() {
        return hourlyPrice;
    }

    public void setHourlyPrice(BigDecimal hourlyPrice) {
        this.hourlyPrice = hourlyPrice;
    }

    public String getParkingName() {
        return parkingName;
    }

    public void setParkingName(String parkingName) {
        this.parkingName = parkingName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Parking{" +
                "id=" + id +
                ", parkingName='" + parkingName + '\'' +
                ", parkingPhoto='" + parkingPhoto + '\'' +
                ", hourlyPrice=" + hourlyPrice +
                ", description='" + description + '\'' +
                ", entrancepointx=" + entrancepointx +
                ", entrancepointy=" + entrancepointy +
                '}';
    }

    //    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parking", optional = false)
//    private ParkingOwner parkingOwner;
//
//    public ParkingOwner getParkingOwner() {
//        return parkingOwner;
//    }
//
//    public void setParkingOwner(ParkingOwner parkingOwner) {
//        this.parkingOwner = parkingOwner;
//    }
}