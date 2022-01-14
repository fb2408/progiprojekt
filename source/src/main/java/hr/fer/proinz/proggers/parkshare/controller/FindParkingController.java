package hr.fer.proinz.proggers.parkshare.controller;

import hr.fer.proinz.proggers.parkshare.dto.MessageDTO;
import hr.fer.proinz.proggers.parkshare.model.Client;
import hr.fer.proinz.proggers.parkshare.model.ClientReservation;
import hr.fer.proinz.proggers.parkshare.model.ClientReservationId;
import hr.fer.proinz.proggers.parkshare.model.Parking;
import hr.fer.proinz.proggers.parkshare.repo.ClientRepository;
import hr.fer.proinz.proggers.parkshare.repo.ClientReservationRepository;
import hr.fer.proinz.proggers.parkshare.repo.ParkingRepository;
import hr.fer.proinz.proggers.parkshare.repo.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class FindParkingController {

    private final UserRepository userRepository;
    private final ClientReservationRepository clientReservationRepository;
    private final ParkingRepository parkingRepository;
    private final ClientRepository clientRepository;

    public FindParkingController(UserRepository userRepository, ClientReservationRepository clientReservationRepository, ParkingRepository parkingRepository, ClientRepository clientRepository) {
        this.userRepository = userRepository;
        this.clientReservationRepository = clientReservationRepository;
        this.parkingRepository = parkingRepository;
        this.clientRepository = clientRepository;
    }

    @GetMapping("/findParking")
    public String showMap(Model model, Authentication auth) {
        boolean loggedIn;
        loggedIn = auth != null;
        model.addAttribute("loggedIn", loggedIn);
        return "findparking";
    }

    @PostMapping("/findParking")
    public String reserveSpot(Model model, Authentication auth, @RequestParam int ownerUserId, @RequestParam int parkingSpotNumber,
                              @RequestParam Instant timeOfStart, @RequestParam int duration, @RequestParam boolean recurring, @RequestParam boolean payNow) {
        ArrayList<MessageDTO> errors = new ArrayList<>();
        ArrayList<MessageDTO> information = new ArrayList<>();
        if(auth == null){
            return "redirect:/loginRouter";
        }
        Integer currentUserModelId = userRepository.findByEmail(auth.getName()).getId();
        List<ClientReservation> clientReservations = clientReservationRepository.findAllByOwnerUserIdAndParkingSpotNumber(currentUserModelId, parkingSpotNumber);
        Instant timeOfEnd = timeOfStart.plusSeconds(3600L * duration);
        for(ClientReservation cr : clientReservations) {
            Instant otherStartTime = cr.getId().getTimeofstart();
            Instant otherEndTime = otherStartTime.plusSeconds(3600L * cr.getDuration());
            if(timeOfStart.isBefore(otherEndTime) && otherStartTime.isBefore(timeOfEnd)) {
                errors.add(new MessageDTO("Can't reserve that parking spot", "The parking spot is already reserved"));
                model.addAttribute("errors", errors);
                return "findparking";
            }
        }
        Optional<Parking> parkingOptional = parkingRepository.findById(ownerUserId);
        Optional<Client> clientOptional = clientRepository.findById(currentUserModelId);
        if(parkingOptional.isEmpty() || clientOptional.isEmpty()) {
            errors.add(new MessageDTO("Server error occurred", "We've had an internal server error, please try again"));
            model.addAttribute("errors", errors);
            return "findparking";
        }

        Parking parking = parkingOptional.get();
        Client client = clientOptional.get();
        double price = duration * parking.getHourlyPrice().doubleValue();
        double clientBalance = client.getWalletBalance().doubleValue();
        if(clientBalance < price) {
            errors.add(new MessageDTO("Insufficient funds!", String.format("You have %,.2fHRK available and the price is %,.2fHRK", clientBalance, price)));
            model.addAttribute("errors", errors);
            return "findparking";
        }
        clientRepository.save(new Client(client.getId(), BigDecimal.valueOf(clientBalance - price)));
        clientReservationRepository.save(new ClientReservation(new ClientReservationId(currentUserModelId, timeOfStart), duration, recurring, parkingSpotNumber, ownerUserId));
        information.add(new MessageDTO("Success!", "Parking reservation successful"));
        model.addAttribute("information", information);
        return "findparking";
    }
}
