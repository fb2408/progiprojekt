package hr.fer.proinz.proggers.parkshare.model.repo;

import hr.fer.proinz.proggers.parkshare.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel, Integer> {

    UserModel findByName(String username);

    boolean existsByEmailOrName(String email, String username);
}
