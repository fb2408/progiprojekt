package hr.fer.proinz.proggers.parkshare.model;

import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Embeddable
public class ParkingSpotOccupancyId implements Serializable {
    @Serial
    private static final long serialVersionUID = 1397568348224966614L;
    @Column(name = "userid", nullable = false)
    private Integer userid;
    @Column(name = "parkingspotnumber", nullable = false)
    private Integer parkingspotnumber;
    @Column(name = "datefrom", nullable = false)
    private Instant datefrom;

    public Instant getDatefrom() {
        return datefrom;
    }

    public void setDatefrom(Instant datefrom) {
        this.datefrom = datefrom;
    }

    public Integer getParkingspotnumber() {
        return parkingspotnumber;
    }

    public void setParkingspotnumber(Integer parkingspotnumber) {
        this.parkingspotnumber = parkingspotnumber;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(parkingspotnumber, datefrom, userid);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ParkingSpotOccupancyId entity = (ParkingSpotOccupancyId) o;
        return Objects.equals(this.parkingspotnumber, entity.parkingspotnumber) &&
                Objects.equals(this.datefrom, entity.datefrom) &&
                Objects.equals(this.userid, entity.userid);
    }
}