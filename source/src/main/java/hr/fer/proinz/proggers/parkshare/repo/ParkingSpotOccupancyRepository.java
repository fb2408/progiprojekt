package hr.fer.proinz.proggers.parkshare.repo;

import hr.fer.proinz.proggers.parkshare.model.ParkingSpotOccupancy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParkingSpotOccupancyRepository extends JpaRepository<ParkingSpotOccupancy, Integer> {

    List<ParkingSpotOccupancy> getAllById_Userid(Integer id);
}
