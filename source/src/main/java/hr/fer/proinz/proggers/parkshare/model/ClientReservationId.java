package hr.fer.proinz.proggers.parkshare.model;

import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Embeddable
public class ClientReservationId implements Serializable {
    @Serial
    private static final long serialVersionUID = -7348700271768085899L;
    @Column(name = "clientuserid", nullable = false)
    private Integer clientuserid;
    @Column(name = "timeofstart", nullable = false)
    private Instant timeofstart;

    public ClientReservationId() {
    }

    public Instant getTimeofstart() {
        return timeofstart;
    }

    public void setTimeofstart(Instant timeofstart) {
        this.timeofstart = timeofstart;
    }

    public Integer getClientuserid() {
        return clientuserid;
    }

    public void setClientuserid(Integer clientuserid) {
        this.clientuserid = clientuserid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeofstart, clientuserid);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ClientReservationId entity = (ClientReservationId) o;
        return Objects.equals(this.timeofstart, entity.timeofstart) &&
                Objects.equals(this.clientuserid, entity.clientuserid);
    }
}