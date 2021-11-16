package hr.fer.proinz.proggers.parkshare.controller;

import com.sun.net.httpserver.HttpPrincipal;
import hr.fer.proinz.proggers.parkshare.dto.UserDTO;
import hr.fer.proinz.proggers.parkshare.model.UserModel;
import hr.fer.proinz.proggers.parkshare.repo.UserRepository;
import hr.fer.proinz.proggers.parkshare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/users")
public class UserController {

    UserService userService;
    UserRepository userRepository;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public String register(UserDTO userDTO, Model model, HttpServletRequest request) {
        try {
            UserModel userModel = userService.registerNewUser(userDTO);
            userService.sendMail(userModel, getSiteURL(request));
        } catch (ResponseStatusException e){
            model.addAttribute("user", new UserDTO());
            model.addAttribute("errorMsg", "Account with given username or email already exists");
            return "register";
        }
        model.addAttribute("checkingMsg", "Please check your email to verify your account!");
        return "login";
    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    @GetMapping("/register/confirm")
    public String verifyUser(@Param("code") String code, Model model) {
        if (userService.verify(code)) {
            model.addAttribute("succesfull_msg", "Congratulations, your account has been verified.");
            return "redirect:login";
        }
        model.addAttribute("user", new UserDTO());
        model.addAttribute("errorMsg", "Sorry, we could not verify account." +
                " It maybe already verified or verification code is incorrect.");
        return "redirect:register";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        model.addAttribute("user", new UserDTO());
        return "register";
    }

    @GetMapping("/details")
    public String showUserDetails(Model model, Authentication auth){
        UserDTO currentUser = new UserDTO(userRepository.findByEmail(auth.getName()));
        model.addAttribute("user", currentUser.toString());
        return  "userDetails";
    }
}