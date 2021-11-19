package hr.fer.proinz.proggers.parkshare.repo;

import hr.fer.proinz.proggers.parkshare.model.ParkingOwner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingOwnerRepository extends JpaRepository<ParkingOwner, Integer> {
    ParkingOwner findById(int id);
}
