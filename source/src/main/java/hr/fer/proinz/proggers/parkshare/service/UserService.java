package hr.fer.proinz.proggers.parkshare.service;

import hr.fer.proinz.proggers.parkshare.dto.UserDTO;
import hr.fer.proinz.proggers.parkshare.model.UserModel;
import hr.fer.proinz.proggers.parkshare.repo.UserRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserService {

    private final EmailService emailService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(EmailService emailService, UserRepository userRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.emailService = emailService;
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
        String verificationCode = RandomString.make(64);
        userModel.setVerificationCode(verificationCode);
        System.out.println(userModel);
        return userRepository.save(userModel);
    }

    public void sendMail(UserModel userModel, String siteURL) {

        String verifyURL = siteURL + "/confirm?code=" + userModel.getVerificationCode();
        emailService.send(userModel.getEmail(),userModel.getName(), verifyURL);
    }

    public boolean verify(String verificationCode) {
        UserModel user = userRepository.findByVerificationCode(verificationCode);

        if (user == null || user.getConfirmed()) {
            return false;
        } else {
            user.setVerificationCode(null);
            user.setConfirmed(true);
            userRepository.save(user);
            return true;
        }
    }
}
