package hr.fer.proinz.proggers.parkshare.service;

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

    public UserModel registerNewUser(UserDTO userDTO) {
        if (userRepository.existsByEmailOrName(userDTO.getUsermail(), userDTO.getUsername()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        UserModel userModel = new UserModel();
        userModel.setEmail(userDTO.getUsermail());
        userModel.setName(userDTO.getUsername());
        userModel.setFirstName(userDTO.getUserfirstname());
        userModel.setSurname(userDTO.getUsersurname());
        userModel.setTempPassword(bCryptPasswordEncoder.encode(userDTO.getTemppassword()));
        userModel.setType(userDTO.getUsertype());
        userModel.setConfirmed(userDTO.isConfirmed());
        System.out.println(userModel);
        return userRepository.save(userModel);
    }
}
