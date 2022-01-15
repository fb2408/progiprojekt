package hr.fer.proinz.proggers.parkshare.repo;

import hr.fer.proinz.proggers.parkshare.model.ParkingSpot;
import hr.fer.proinz.proggers.parkshare.model.ParkingSpotId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;

public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, ParkingSpotId> {
    @Query(value = """
            SELECT * FROM parkingspot p
            WHERE exists(
                SELECT * FROM clientreservation cr
                WHERE cr.owneruserid = p.userid AND
                 NOT (cr.timeofstart < cast(?1 AS timestamp) + CAST(CONCAT(?2, ' hours') AS interval)
                    AND CAST(?1 AS timestamp) < cr.timeofstart + CAST(CONCAT(cr.duration, ' hours') AS interval))
            ) AND p.userid = ?3
""", nativeQuery = true)
    List<ParkingSpot> findAvailabeById(Instant start, double duration, Integer id);

    Page<ParkingSpot> findAllById_Userid(int id, Pageable pageable);

    List<ParkingSpot> findAllById_Userid(int id);

    boolean existsById(ParkingSpotId parkingSpotId);

    ParkingSpot findById(ParkingSpotId parkingSpotId);

    @Transactional
    Long deleteById(ParkingSpotId id);
}
