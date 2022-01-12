package hr.fer.proinz.proggers.parkshare.repo;

import hr.fer.proinz.proggers.parkshare.model.ParkingSpot;
import hr.fer.proinz.proggers.parkshare.model.ParkingSpotId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Integer> {

    Page<ParkingSpot> findAllById_Userid(int id, Pageable pageable);
}
