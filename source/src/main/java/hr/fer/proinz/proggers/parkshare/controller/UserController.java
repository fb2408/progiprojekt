package hr.fer.proinz.proggers.parkshare.controller;

import hr.fer.proinz.proggers.parkshare.dto.*;
import hr.fer.proinz.proggers.parkshare.model.Parking;
import hr.fer.proinz.proggers.parkshare.model.ParkingSpot;
import hr.fer.proinz.proggers.parkshare.model.ParkingSpotId;
import hr.fer.proinz.proggers.parkshare.model.UserModel;
import hr.fer.proinz.proggers.parkshare.repo.*;
import hr.fer.proinz.proggers.parkshare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class UserController {

    UserService userService;
    UserRepository userRepository;
    ParkingOwnerRepository ownerRepository;
    ClientRepository clientRepository;
    ParkingRepository parkingRepository;
    ParkingSpotRepository parkingSpotRepository;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository,
                          ParkingOwnerRepository ownerRepository, ClientRepository clientRepository,
                          ParkingRepository parkingRepository, ParkingSpotRepository parkingSpotRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.ownerRepository = ownerRepository;
        this.clientRepository = clientRepository;
        this.parkingRepository = parkingRepository;
        this.parkingSpotRepository = parkingSpotRepository;
    }

    @PostMapping("/")
    public ModelAndView register(RegisterFormDTO registerFormDTO, ModelMap model, HttpServletRequest request) {
        ArrayList<MessageDTO> errors = new ArrayList<>();
        ArrayList<MessageDTO> information = new ArrayList<>();
        UserModel registered;
        try {
            registered = userService.registerNewUser(registerFormDTO);
            if (registered.getType().equals("client")) {
                userService.sendMail(registered, getSiteURL(request));
            } else {
                ownerRepository.findById(registered.getId()).ifPresent((owner) -> {
                    owner.setIban(registerFormDTO.getIban());
                    ownerRepository.save(owner);
                });
            }
        } catch (ResponseStatusException e) {
            model.addAttribute("registerForm", new RegisterFormDTO());
            errors.add(new MessageDTO("Registration failed!",
                    "Account with given username or email already exists."));
            model.addAttribute("errors", errors);
            return new ModelAndView("index", model);
        }
        model.addAttribute("registerForm", new RegisterFormDTO());
        information.add(new MessageDTO("Registration successful!",
                registered.getType().equals("owner") ?
                        "Please wait for an administrator to confirm your registration." :
                        "To login, please confirm your account by email."));
        model.addAttribute("information", information);
        return new ModelAndView("index", model);
    }

    private String getSiteURL(HttpServletRequest request) {
        return request.getRequestURL().toString();
    }

    @GetMapping("/confirm")
    public String verifyUser(@Param("code") String code, Model model) {
        if (userService.verify(code)) {
            return "redirect:/?verifySuccess=true";
        }
        return "redirect:/?verifyFailure=true";
    }

    @GetMapping("/")
    public String showRegistrationForm(Model model, Authentication auth) {
        if (auth != null) {
            return "redirect:/loginRouter";
        }
        model.addAttribute("registerForm", new RegisterFormDTO());
        return "index";
    }

    @GetMapping("/profile")
    public String showUserDetails(Model model, Authentication auth) {
        boolean loggedIn;
        loggedIn = auth != null;
        model.addAttribute("loggedIn", loggedIn);
        assert auth != null;
        UserDTO currentUser = userService.UserToDTO(userRepository.findByEmail(auth.getName()));
        model.addAttribute("user", currentUser);
        if(currentUser.isOwner()) {
            boolean hasParking = false;
            Optional<Parking> ownerParking = parkingRepository.findById(currentUser.getId());
            if(ownerParking.isPresent()) {
                hasParking = true;
            }
            model.addAttribute("hasParking", hasParking);
        }
        return "userpage";
    }


    @PostMapping("/profile")
    public ModelAndView editUserDetails(UserDTO updatedUser, ModelMap model, Authentication auth) {
        ArrayList<MessageDTO> errors = new ArrayList<>();
        ArrayList<MessageDTO> information = new ArrayList<>();
        UserModel userModel;
        //TODO make it pretty lmao
        try {
            userModel = userService.updateUser(updatedUser, false, userRepository.findByEmail(auth.getName()).getId());
        } catch (InvalidParameterException i) {
            errors.add(new MessageDTO("Update failed!",
                    "Old password isn't correct."));
            model.addAttribute("errors", errors);
            UserModel currentUserModel = userRepository.findByEmail(auth.getName());
            model.addAttribute("user", userService.UserToDTO(currentUserModel));
            return new ModelAndView("userpage", model);
        } catch (ResponseStatusException e) {
            errors.add(new MessageDTO("Update failed!",
                    "Account with given username already exists."));
            model.addAttribute("errors", errors);
            UserModel currentUserModel = userRepository.findByEmail(auth.getName());
            model.addAttribute("user", userService.UserToDTO(currentUserModel));
            return new ModelAndView("userpage", model);
        }

        model.addAttribute("user", userService.UserToDTO(userModel));
        information.add(new MessageDTO("Success!", "Your data has been updated."));
        model.addAttribute("information", information);
        System.out.println(updatedUser);
        updatedUser.setRole(userModel.getType());
        return new ModelAndView("userpage", model);
    }

    @PostMapping("/profile/createParking")
    public String createParking(CreateParkingDTO parking, ModelMap model, Authentication auth) {
        ArrayList<MessageDTO> errors = new ArrayList<>();
        ArrayList<MessageDTO> information = new ArrayList<>();
        UserModel currentUserModel = userRepository.findByEmail(auth.getName());
        try {
            Integer id = userRepository.findByEmail(auth.getName()).getId();
            if (!parkingRepository.existsById(id)) {
                Parking newParking = new Parking();
                newParking.setParkingName(parking.getParkingName());
                newParking.setHourlyPrice(parking.getHourlyPrice());
                newParking.setParkingPhoto(parking.getParkingPhoto());
                newParking.setDescription(parking.getDescription());
                newParking.setId(id);
          // TODO add x and y coordinates in newParking
                parkingRepository.save(newParking);
                information.add(new MessageDTO("Parking created successfuly", ""));
                model.addAttribute("information", information);
                model.addAttribute("user", userService.UserToDTO(currentUserModel));
                //TODO : implement add parking spot
//                return new ModelAndView("createParkingSpot", model);
                return "redirect:/profile";
            } else {
                errors.add(new MessageDTO("Parking already exists", "You can have only one parking"));
                model.addAttribute("errors", errors);
            }
        } catch(Exception exc) {
//            errors.add(new MessageDTO("Error happend", exc.getMessage()));
//            model.addAttribute("errors", errors);
//            model.addAttribute("user", userService.UserToDTO(currentUserModel));
            return "redirect:/profile";
        }
//        model.addAttribute("user", userService.UserToDTO(currentUserModel));
        return "redirect:/profile";
    }


    @GetMapping("/profile/createParking")
    public String createParkingForm(Model model, Authentication auth) {
        UserModel currentUser = userRepository.findByEmail(auth.getName());
        if (currentUser.isOwner() && !parkingRepository.existsById(currentUser.getId())) {
            model.addAttribute("parking", new CreateParkingDTO());
            return "createParking";
        } else {
           return "redirect:/profile";
        }
    }

    @GetMapping("/profile/editParking")
    public String editParkingForm(ModelMap model, Authentication auth,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "5") int size) {
        UserModel currentUser = userRepository.findByEmail(auth.getName());
        Parking parking = parkingRepository.findById(currentUser.getId()).orElse(null);
        if (currentUser.isOwner() && parking != null) {
            Page<ParkingSpot> spotPage = userService.getParkingSpotPage(currentUser.getId(), page, size);

            int pageCount = spotPage.getTotalPages();
            List<Integer> pageNumbers;
            if (pageCount <= 9) {
                pageNumbers = IntStream.rangeClosed(1, pageCount)
                        .boxed().collect(Collectors.toList());
            } else {
                int startNumber = Math.max(1, page - 4);
                int endNumber = Math.min(pageCount, page + 4);
                pageNumbers = IntStream.rangeClosed(startNumber, endNumber)
                        .boxed().collect(Collectors.toList());
            }
            model.addAttribute("spotPage", spotPage);
            model.addAttribute("pageNumbers", pageNumbers);
            model.addAttribute("parking", new CreateParkingDTO(parking));
            return "editparking";
        } else {
            return "redirect:/profile";
        }
    }

    @PostMapping("/profile/editParking")
    public String editParking(CreateParkingDTO parking, ModelMap model, Authentication auth) {
        ArrayList<MessageDTO> errors = new ArrayList<>();
        ArrayList<MessageDTO> information = new ArrayList<>();
        UserModel currentUserModel = userRepository.findByEmail(auth.getName());
        try {
            Integer id = userRepository.findByEmail(auth.getName()).getId();
            if (parkingRepository.existsById(id)) {
                Parking newParking = parkingRepository.getById(id);
                if(parking.getParkingName() != null) {
                    newParking.setParkingName(parking.getParkingName());
                }
                if(parking.getHourlyPrice() != null) {
                    newParking.setHourlyPrice(parking.getHourlyPrice());
                }
                // TODO : image file handling
//                newParking.setParkingPhoto(parking.getParkingPhoto());
                if(parking.getDescription() != null) {
                    newParking.setDescription(parking.getDescription());
                }
                // TODO add x and y coordinates in newParking
                parkingRepository.save(newParking);
                information.add(new MessageDTO("Parking edited successfully", ""));
                model.addAttribute("information", information);
                model.addAttribute("user", userService.UserToDTO(currentUserModel));
                //TODO : implement add parking spot
//                return new ModelAndView("createParkingSpot", model);
                model.addAttribute("parking", new CreateParkingDTO(newParking));
                return "redirect:/profile/editParking";
            } else {
                return "redirect:/profile/createParking";
            }
        } catch(Exception exc) {
//            errors.add(new MessageDTO("Error happend", exc.getMessage()));
//            model.addAttribute("errors", errors);
//            model.addAttribute("user", userService.UserToDTO(currentUserModel));
            return "redirect:/profile";
        }
//        model.addAttribute("user", userService.UserToDTO(currentUserModel));
//        return "redirect:/profile";
    }

    @GetMapping("/profile/createParkingSpot")
    public String showCreateParkingSpot (Model model, Authentication auth) {
        if(auth == null){
            return "redirect:/loginRouter";
        }
        UserModel currentUser = userRepository.findByEmail(auth.getName());
        model.addAttribute("spot", new ParkingSpotDTO());
        if(currentUser.isOwner()) {
            return "addParkingSpot";
        } else {
            return "redirect:/profile";
        }
    }

    @PostMapping("/profile/createParkingSpot")
    public String createParkingSpot (ParkingSpotDTO parkingSpotDTO, Authentication auth, ModelMap model, @RequestParam(value="submitSpot") String continueOrFinish) {
        ArrayList<MessageDTO> errors = new ArrayList<>();
        ArrayList<MessageDTO> information = new ArrayList<>();
        if(auth == null){
            return "redirect:/loginRouter";
        }
        UserModel currentUser = userRepository.findByEmail(auth.getName());
        OptionalInt maxParkingSpotIdOptional = parkingSpotRepository.findAllById_Userid(currentUser.getId()).stream().mapToInt(parkingSpot -> parkingSpot.getId().getParkingspotnumber()).max();
        int maxParkingSpotId;
        if(maxParkingSpotIdOptional.isEmpty()){
            maxParkingSpotId = 0;
        } else {
            maxParkingSpotId = maxParkingSpotIdOptional.getAsInt();
        }
        parkingSpotDTO.setId(new ParkingSpotId(currentUser.getId(), maxParkingSpotId + 1));
        information.add(new MessageDTO("Success!", "Parking spot successfully added"));
        model.addAttribute(information);
        parkingSpotRepository.save(new ParkingSpot(parkingSpotDTO));
        if(continueOrFinish.equals("continue")){
            return "redirect:/profile/createParkingSpot";
        } else {
            return "redirect:/profile/editParking";
        }
    }

    @PostMapping("profile/editParkingSpot")
    public String editParkingSpot (ParkingSpotDTO parkingSpotDTO, Authentication auth, ModelMap model, @RequestParam(value= "id") Integer parkingSpotNumber){
        ArrayList<MessageDTO> errors = new ArrayList<>();
        ArrayList<MessageDTO> information = new ArrayList<>();
        if(auth == null){
            return "redirect:/loginRouter";
        }
        Integer currentUserId = userRepository.findByEmail(auth.getName()).getId();
        if(!parkingSpotRepository.existsById(new ParkingSpotId(currentUserId, parkingSpotNumber))) {
            errors.add(new MessageDTO("Can't edit parking spot", "Can't find the parking spot to edit"));
            return "redirect:/profile/editParking";
        }
        parkingSpotDTO.setId(new ParkingSpotId(currentUserId, parkingSpotNumber));
        information.add(new MessageDTO("Success!", "Parking spot successfully edited"));
        model.addAttribute(information);
        parkingSpotRepository.save(new ParkingSpot(parkingSpotDTO));
        return "redirect:/profile/editParking";
    }

    @GetMapping("/profile/editParkingSpot")
    public String editParkingSpot (Model model, Authentication auth) {
        if(auth == null){
            return "redirect:/loginRouter";
        }
        UserModel currentUser = userRepository.findByEmail(auth.getName());
        model.addAttribute("spot", new ParkingSpotDTO());
        if(currentUser.isOwner()) {
            return "editParkingSpot";
        } else {
            return "redirect:/profile";
        }
    }

    @PostMapping("profile/deleteParkingSpot")
    public String deleteParkingSpot (Authentication auth, @RequestParam(value= "id") Integer parkingSpotNumber) {
        ArrayList<MessageDTO> errors = new ArrayList<>();
        ArrayList<MessageDTO> information = new ArrayList<>();
        if(auth == null){
            return "redirect:/loginRouter";
        }
        Integer currentUserId = userRepository.findByEmail(auth.getName()).getId();
        if(!parkingSpotRepository.existsById(new ParkingSpotId(currentUserId, parkingSpotNumber))) {
            errors.add(new MessageDTO("Can't delete parking spot", "Can't find the parking spot to delete"));
            return "redirect:/profile/editParking";
        }
        parkingSpotRepository.deleteById(new ParkingSpotId(currentUserId, parkingSpotNumber));
        information.add(new MessageDTO("Success!", "Parking spot successfully deleted"));
        return "redirect:/profile/editParking";
    }
}