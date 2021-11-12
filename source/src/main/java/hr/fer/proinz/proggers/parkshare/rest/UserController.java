package hr.fer.proinz.proggers.parkshare.rest;

import hr.fer.proinz.proggers.parkshare.dto.UserDTO;
import hr.fer.proinz.proggers.parkshare.model.UserModel;
import hr.fer.proinz.proggers.parkshare.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserController(UserRepository userRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/register")
    public void register(@RequestBody UserDTO userDTO) {
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
        userRepository.save(userModel);
    }

}
