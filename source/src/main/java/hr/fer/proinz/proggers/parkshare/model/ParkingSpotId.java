package hr.fer.proinz.proggers.parkshare.model;

import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ParkingSpotId implements Serializable {
    @Serial
    private static final long serialVersionUID = 7997774114776680649L;
    @Column(name = "userid", nullable = false)
    private Integer userid;
    @Column(name = "parkingspotnumber", nullable = false)
    private Integer parkingspotnumber;

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
        return Objects.hash(parkingspotnumber, userid);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ParkingSpotId entity = (ParkingSpotId) o;
        return Objects.equals(this.parkingspotnumber, entity.parkingspotnumber) &&
                Objects.equals(this.userid, entity.userid);
    }
}