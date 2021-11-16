package hr.fer.proinz.proggers.parkshare.repo;

import hr.fer.proinz.proggers.parkshare.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<UserModel, Integer> {

    UserModel findByName(String username);

    UserModel findByEmail(String email);

    boolean existsByEmailOrName(String email, String username);

    @Query("SELECT u FROM UserModel u WHERE u.verificationCode = ?1")
    public UserModel findByVerificationCode(String code);
}
