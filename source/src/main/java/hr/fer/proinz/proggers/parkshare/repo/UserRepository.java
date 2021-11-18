package hr.fer.proinz.proggers.parkshare.repo;

import hr.fer.proinz.proggers.parkshare.model.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<UserModel, Integer> {

    UserModel findByName(String username);

    UserModel findByEmail(String email);

    boolean existsByEmailOrName(String email, String username);

    Page<UserModel> findByTypeNotLike(String type, Pageable pageable);
//    UserModel findByVerificationCode(String code);

    List<UserModel> findByConfirmedAndType(boolean confirmed, String type);
}
