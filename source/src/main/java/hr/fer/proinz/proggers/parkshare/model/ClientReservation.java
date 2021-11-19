package hr.fer.proinz.proggers.parkshare.model;

import javax.persistence.*;

@Table(name = "clientreservation", indexes = {
        @Index(name = "clientreservation_owneruserid_parkingspotnumber_timeofstart_key", columnList = "owneruserid, parkingspotnumber, timeofstart", unique = true)
})
@Entity
public class ClientReservation {
    @EmbeddedId
    private ClientReservationId id;

    @JoinColumns({
            @JoinColumn(name = "owneruserid", referencedColumnName = "userid", nullable = false),
            @JoinColumn(name = "parkingspotnumber", referencedColumnName = "parkingspotnumber", nullable = false)
    })
    @ManyToOne(optional = false)
    private ParkingSpot parkingSpot;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    @Column(name = "recurring", nullable = false)
    private Boolean recurring = false;

    public Boolean getRecurring() {
        return recurring;
    }

    public void setRecurring(Boolean recurring) {
        this.recurring = recurring;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public void setParkingSpot(ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
    }

    public ClientReservationId getId() {
        return id;
    }

    public void setId(ClientReservationId id) {
        this.id = id;
    }
}