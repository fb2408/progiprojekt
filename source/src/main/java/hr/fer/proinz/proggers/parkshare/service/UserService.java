package hr.fer.proinz.proggers.parkshare.service;

import hr.fer.proinz.proggers.parkshare.dto.RegisterFormDTO;
import hr.fer.proinz.proggers.parkshare.dto.UserDTO;
import hr.fer.proinz.proggers.parkshare.model.ParkingOwner;
import hr.fer.proinz.proggers.parkshare.model.UserModel;
import hr.fer.proinz.proggers.parkshare.repo.UserRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;

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

    public UserModel registerNewUser(RegisterFormDTO registerFormDTO) {
        if (userRepository.existsByEmailOrName(registerFormDTO.getUserMail(), registerFormDTO.getUsername()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        UserModel userModel = new UserModel();
        userModel.setEmail(registerFormDTO.getUserMail());
        userModel.setName(registerFormDTO.getUsername());
        userModel.setFirstName(registerFormDTO.getUserFirstName());
        userModel.setSurname(registerFormDTO.getUserSurname());
        userModel.setTempPassword(bCryptPasswordEncoder.encode(registerFormDTO.getPassword()));
        userModel.setConfirmed(false);
        //TODO SAVE SPECIALISATION TABLE ENTRY
        if(registerFormDTO.getIsOwner()) {
            userModel.setType("owner");
        } else {
            userModel.setType("client");
        }
        System.out.println(userModel);
        return userRepository.save(userModel);
    }

    public void sendMail(UserModel userModel, String siteURL) {

        String verifyURL = siteURL + "confirm?code=" + userModel.getId();
        emailService.send(userModel.getEmail(), userModel.getName(), verifyURL);

    }

    public boolean verify(String verificationCode) {
        UserModel userModel = userRepository.findById(Integer.parseInt(verificationCode)).orElse(null);

        //TODO REMOVE WHEN IMPLEMENT DOUBLE VERIFICATION(MAIL AND ADMIN) FOR OWNER
        if (userModel == null || userModel.getConfirmed() || userModel.getType().equals("owner")) {
            return false;
        } else {
//            userModel.setVerificationCode(null);
            userModel.setConfirmed(true);
            userRepository.save(userModel);

            return true;
        }
    }

    public Page<UserModel> getUserPage(int pageNumber, int pageSize) {
        return userRepository.findByTypeNotLike("admin", PageRequest.of(pageNumber, pageSize,
                Sort.by(Sort.Direction.ASC, "confirmed").and(Sort.by(Sort.Direction.DESC, "type")))
        );
    }

    public List<UserModel> getAllUnconfirmedOwners(){
        return userRepository.findByConfirmedAndType(false, "owner");
    }
}
