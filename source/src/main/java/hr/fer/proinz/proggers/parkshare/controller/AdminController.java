package hr.fer.proinz.proggers.parkshare.controller;

import hr.fer.proinz.proggers.parkshare.dto.MessageDTO;
import hr.fer.proinz.proggers.parkshare.dto.UserDTO;
import hr.fer.proinz.proggers.parkshare.model.UserModel;
import hr.fer.proinz.proggers.parkshare.repo.ParkingOwnerRepository;
import hr.fer.proinz.proggers.parkshare.repo.UserRepository;
import hr.fer.proinz.proggers.parkshare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class AdminController {

    UserService userService;
    UserRepository userRepository;
    ParkingOwnerRepository ownerRepository;

    @Autowired
    public AdminController(UserService userService, UserRepository userRepository, ParkingOwnerRepository ownerRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.ownerRepository = ownerRepository;
    }

    @GetMapping("/admin")
    public String getAdminPage(Model model, Authentication auth,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "5") int size) {
        Page<UserDTO> userPage = userService.getUserPage(page, size).map(user -> userService.UserToDTO(user));
        model.addAttribute("userPage", userPage);
        model.addAttribute("loggedIn", true);
        int pageCount = userPage.getTotalPages();
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
        model.addAttribute("pageNumbers", pageNumbers);
        List<UserDTO> unconfirmedOwners = userService.getAllUnconfirmedOwners().stream().map(user -> userService.UserToDTO(user)).collect(Collectors.toList());
        model.addAttribute("unconfirmedOwners", unconfirmedOwners);

        UserDTO currentUser = userService.UserToDTO(userRepository.findByEmail(auth.getName()));
        UserDTO emptyUser = new UserDTO();
        model.addAttribute("user", currentUser);
        model.addAttribute("emptyUser", emptyUser);
        return "adminpage";
    }

    @PostMapping("/admin")
    public ModelAndView editAdminUserDetails(UserDTO updatedUser, ModelMap model, Authentication auth) {
        ArrayList<MessageDTO> errors = new ArrayList<>();
        ArrayList<MessageDTO> information = new ArrayList<>();
        //TODO make it pretty lmao
        try {
            userService.updateUser(updatedUser, false, userRepository.findByEmail(auth.getName()).getId());
        } catch (InvalidParameterException i) {
            errors.add(new MessageDTO("Update failed!",
                    "Old password isn't correct."));
            model.addAttribute("errors", errors);
            UserModel currentUserModel = userRepository.findByEmail(auth.getName());
            model.addAttribute("user", userService.UserToDTO(currentUserModel));
            return new ModelAndView("redirect:/admin", model);
        } catch (ResponseStatusException e) {
            errors.add(new MessageDTO("Update failed!",
                    "Account with given username already exists."));
            model.addAttribute("errors", errors);
            UserModel currentUserModel = userRepository.findByEmail(auth.getName());
            model.addAttribute("user", userService.UserToDTO(currentUserModel));
            return new ModelAndView("redirect:/admin", model);
        }
        model.addAttribute("user", updatedUser);
        information.add(new MessageDTO("Success!", "Your data has been updated."));
        model.addAttribute("information", information);
        return new ModelAndView("redirect:/admin", model);
    }

    @GetMapping("admin/confirmOwner/{id}")
    public String adminConfirmOwner(@PathVariable(name = "id") int id) {
        userService.confirmOwner(id);
        return "redirect:/admin?ownerConfirmed=true";
    }

    @PostMapping("/admin/changeuser")
    public String adminEditUserDetails(UserDTO updatedUser) {
        System.out.println("ovo "+updatedUser.getId());
        try {
            userService.updateUser(updatedUser, true, updatedUser.getId());
        } catch (NoSuchElementException n) {
            return "redirect:/admin?invalidId=true";
        } catch (ResponseStatusException e) {
            return "redirect:/admin?userWithGivenUsernameExists=true";
        }
        return "redirect:/admin?dataUpdated=true";
    }
}
