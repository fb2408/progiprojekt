package hr.fer.proinz.proggers.parkshare.repo;

import hr.fer.proinz.proggers.parkshare.model.ClientReservation;
import hr.fer.proinz.proggers.parkshare.model.ClientReservationId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientReservationRepository extends JpaRepository<ClientReservation, ClientReservationId> {
}
