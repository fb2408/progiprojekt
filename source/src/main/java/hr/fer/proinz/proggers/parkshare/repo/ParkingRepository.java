package hr.fer.proinz.proggers.parkshare.repo;

import hr.fer.proinz.proggers.parkshare.model.Parking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;


public interface ParkingRepository extends JpaRepository<Parking, Integer> {
    @Query(value = """
            SELECT * FROM
            parking p
            WHERE EXISTS(
                SELECT * FROM parkingspot ps
                WHERE ps.userid = p.userid AND ps.parkingspottype = ?1 AND ps.canbereserved
            )
            """, nativeQuery = true)
    List<Parking> findAvailable(String type);
}


/*AND
                 (cr.timeofstart < cast(?1 AS timestamp) + CAST(CONCAT(?2, ' hours') AS interval)
                    AND CAST(?1 AS timestamp) < cr.timeofstart + CAST(CONCAT(cr.duration, ' hours') AS interval))*/