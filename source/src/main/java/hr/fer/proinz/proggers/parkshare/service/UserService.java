package hr.fer.proinz.proggers.parkshare.service;

import hr.fer.proinz.proggers.parkshare.dto.RegisterFormDTO;
import hr.fer.proinz.proggers.parkshare.dto.UserDTO;
import hr.fer.proinz.proggers.parkshare.model.UserModel;
import hr.fer.proinz.proggers.parkshare.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public UserModel registerNewUser(RegisterFormDTO registerFormDTO) {
        if (userRepository.existsByEmailOrName(registerFormDTO.getUserMail(), registerFormDTO.getUsername()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        UserModel userModel = new UserModel();
        userModel.setEmail(registerFormDTO.getUserMail());
        userModel.setName(registerFormDTO.getUsername());
        userModel.setFirstName(registerFormDTO.getUserFirstName());
        userModel.setSurname(registerFormDTO.getUserSurname());
        userModel.setTempPassword(bCryptPasswordEncoder.encode(registerFormDTO.getPassword()));
        userModel.setType(registerFormDTO.getIsOwner() ? "owner" : "client");
        userModel.setConfirmed(registerFormDTO.isConfirmed());
        System.out.println(userModel);
        return userRepository.save(userModel);
    }
}
