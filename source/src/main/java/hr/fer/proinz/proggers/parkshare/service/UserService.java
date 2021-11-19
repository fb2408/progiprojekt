package hr.fer.proinz.proggers.parkshare.service;

import hr.fer.proinz.proggers.parkshare.dto.RegisterFormDTO;
import hr.fer.proinz.proggers.parkshare.dto.UserDTO;
import hr.fer.proinz.proggers.parkshare.model.Client;
import hr.fer.proinz.proggers.parkshare.model.ParkingOwner;
import hr.fer.proinz.proggers.parkshare.model.UserModel;
import hr.fer.proinz.proggers.parkshare.repo.ClientRepository;
import hr.fer.proinz.proggers.parkshare.repo.ParkingOwnerRepository;
import hr.fer.proinz.proggers.parkshare.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import javax.xml.bind.ValidationException;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    private final ClientRepository clientRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final ParkingOwnerRepository parkingOwnerRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    public UserService(ClientRepository clientRepository, EmailService emailService, UserRepository userRepository, ParkingOwnerRepository parkingOwnerRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.clientRepository = clientRepository;
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.parkingOwnerRepository = parkingOwnerRepository;
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
        if (registerFormDTO.getIsOwner()) {
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

    public void confirmOwner(int ownerId) {
        Optional<UserModel> owner = userRepository.findById(ownerId);
        owner.ifPresent(userModel -> userModel.setConfirmed(true));
    }

    public UserModel updateUser(UserDTO userDTO, boolean isAdmin, int id) {

        if (userRepository.existsByName(userDTO.getUsername()) && userRepository.findByName(userDTO.getUsername()).getId() != id) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        System.out.println(userRepository.getById(id).getTempPassword());
        System.out.println((userDTO.getConfirmationPassword()));
        if (!isAdmin && !Objects.equals(userDTO.getConfirmationPassword(), "") && !bCryptPasswordEncoder.matches((userDTO.getConfirmationPassword()), userRepository.getById(id).getTempPassword())) {
            throw new InvalidParameterException();
        }
        Optional<UserModel> optionalUserModel = userRepository.findById(id);
        if (optionalUserModel.isEmpty())
            throw new NoSuchElementException("There is no user with given id");

        UserModel userModel = optionalUserModel.get();
        System.out.println(userModel);
        userModel.setName(userDTO.getUsername());
        userModel.setFirstName(userDTO.getFirstName());
        userModel.setSurname(userDTO.getLastName());
        System.out.println("pass: " + userDTO.getPassword());
        System.out.println("confPass: " + userDTO.getConfirmationPassword());
        if (isAdmin || (!Objects.equals(userDTO.getConfirmationPassword(), "") && userDTO.getConfirmationPassword() != null)) {
            if((!Objects.equals(userDTO.getPassword(), "") && userDTO.getPassword() != null)) {
                userModel.setTempPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
            }
        }

        if (userDTO.getIban() != null)
            parkingOwnerRepository.findById(userModel.getId()).ifPresent((parkingOwner) -> {
                parkingOwner.setIban(userDTO.getIban());
                parkingOwnerRepository.save(parkingOwner);
            });
        return userRepository.save(userModel);
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

    public List<UserModel> getAllUnconfirmedOwners() {
        return userRepository.findByConfirmedAndType(false, "owner");
    }

    public UserDTO UserToDTO(UserModel model) {
        if (model.getType().equals("owner")) {
            ParkingOwner associatedOwner = parkingOwnerRepository.findById(model.getId()).orElse(null);
            assert associatedOwner != null;
            return new UserDTO(model.getId(), model.getName(), model.getFirstName(), model.getSurname(),
                    model.getEmail(), model.getTempPassword(), model.getType(), associatedOwner.getIban(), null, model.getConfirmed());
        } else if (model.getType().equals("client")) {
            Client associatedClient = clientRepository.findById(model.getId()).orElse(null);
            assert associatedClient != null;
            return new UserDTO(model.getId(), model.getName(), model.getFirstName(), model.getSurname(), model.getEmail(),
                    model.getTempPassword(), model.getType(), null, associatedClient.getWalletBalance(), model.getConfirmed());
        }
        return new UserDTO(model.getId(), model.getName(), model.getFirstName(), model.getSurname(), model.getEmail(),
                model.getTempPassword(), model.getType(), null, null, model.getConfirmed());
    }
}
