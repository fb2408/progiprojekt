package hr.fer.proinz.proggers.parkshare.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FindParkingController {
    @GetMapping("/findParking")
    public String showMap(Model model, Authentication auth) {
        boolean loggedIn;
        loggedIn = auth != null;
        model.addAttribute("loggedIn", loggedIn);
        return "findparking";
    }
}
