package hr.fer.proinz.proggers.parkshare.model;

import javax.persistence.*;
import java.time.Instant;

@Table(name = "parkingspotoccupancy", indexes = {
        @Index(name = "parkingspotoccupancy_userid_parkingspotnumber_dateto_key", columnList = "userid, parkingspotnumber, dateto", unique = true)
})
@Entity
public class ParkingSpotOccupancy {
    @EmbeddedId
    private ParkingSpotOccupancyId id;

    @Column(name = "dateto")
    private Instant dateTo;

    @Column(name = "occupancy", nullable = false)
    private Boolean occupancy = false;

    public Boolean getOccupancy() {
        return occupancy;
    }

    public void setOccupancy(Boolean occupancy) {
        this.occupancy = occupancy;
    }

    public Instant getDateTo() {
        return dateTo;
    }

    public void setDateTo(Instant dateTo) {
        this.dateTo = dateTo;
    }

    public ParkingSpotOccupancyId getId() {
        return id;
    }

    public void setId(ParkingSpotOccupancyId id) {
        this.id = id;
    }
}