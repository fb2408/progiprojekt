package hr.fer.proinz.proggers.parkshare.repo;

import hr.fer.proinz.proggers.parkshare.model.ClientReservation;
import hr.fer.proinz.proggers.parkshare.model.ClientReservationId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientReservationRepository extends JpaRepository<ClientReservation, ClientReservationId> {
    List<ClientReservation> findAllByOwnerUserIdAndParkingSpotNumber(Integer id, Integer parkingSpotNumber);
    List<ClientReservation> findAllByOwnerUserId(Integer id);
    List<ClientReservation> findAllById_Clientuserid(Integer userId);
}