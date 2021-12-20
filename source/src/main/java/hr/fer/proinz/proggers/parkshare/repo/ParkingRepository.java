package hr.fer.proinz.proggers.parkshare.repo;

import hr.fer.proinz.proggers.parkshare.model.Parking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParkingRepository extends JpaRepository<Parking, Integer> {

}
