package hr.fer.proinz.proggers.parkshare.controller;

import hr.fer.proinz.proggers.parkshare.dto.UserDTO;
import hr.fer.proinz.proggers.parkshare.model.UserModel;
import hr.fer.proinz.proggers.parkshare.repo.UserRepository;
import hr.fer.proinz.proggers.parkshare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class UserController {

    UserService userService;
    UserRepository userRepository;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public String register(UserDTO userDTO, Model model) {
        try {
            //Ovo je ovde samo da kod radi trenutno
            //TODO refactor
            String type = userDTO.getIsOwner() ? "ROLE_OWNER" : "ROLE_CLIENT";
            userDTO.setUsertype(type);

            UserModel registered = userService.registerNewUser(userDTO);
        } catch (ResponseStatusException e){
            model.addAttribute("user", new UserDTO());
            model.addAttribute("errExists", "Account with given username or email already exists");
            return "index";
        }
        return "index";
    }

    @GetMapping("/")
    public String showRegistrationForm(Model model){
        model.addAttribute("user", new UserDTO());
        return "index";
    }

    @GetMapping("/profile")
    public String showUserDetails(Model model, Authentication auth){
        UserDTO currentUser = new UserDTO(userRepository.findByEmail(auth.getName()));
        model.addAttribute("user", currentUser);
        return  "userpage";
    }
}
