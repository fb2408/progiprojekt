package hr.fer.proinz.proggers.parkshare.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import hr.fer.proinz.proggers.parkshare.dto.MessageDTO;
import hr.fer.proinz.proggers.parkshare.model.*;
import hr.fer.proinz.proggers.parkshare.model.osrm.RoutingResponse;
import hr.fer.proinz.proggers.parkshare.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    private final ParkingSpotRepository parkingSpotRepository;

    @Autowired
    public FindParkingController(UserRepository userRepository, ClientReservationRepository clientReservationRepository, ParkingRepository parkingRepository, ClientRepository clientRepository, ParkingSpotRepository parkingSpotRepository) {
        this.userRepository = userRepository;
        this.clientReservationRepository = clientReservationRepository;
        this.parkingRepository = parkingRepository;
        this.clientRepository = clientRepository;
        this.parkingSpotRepository = parkingSpotRepository;
    }

    @GetMapping("/about")
    public String aboutUs(Model model, Authentication auth) {
        boolean loggedIn;
        loggedIn = auth != null;
        model.addAttribute("loggedIn", loggedIn);
        return "aboutus";
    }

    @GetMapping("/findParking")
    public String showMap(Model model, Authentication auth) {
        List<MessageDTO> errors = new ArrayList<>();
        List<MessageDTO> information = new ArrayList<>();
        if(model.getAttribute("noRoute") != null && (boolean)model.getAttribute("noRoute")) {
            information.add(new MessageDTO("No route found.", ""));
        }
        if(model.getAttribute("noneAvailable") != null && (boolean)model.getAttribute("noneAvailable")) {
            errors.add(new MessageDTO("No parking spots Available!", ""));
        }
        boolean loggedIn;
        loggedIn = auth != null;
        model.addAttribute("loggedIn", loggedIn);
        model.addAttribute("errors", errors);
        model.addAttribute("information", information);
        model.addAttribute("allParkingSpots", parkingSpotRepository.findAll());
        model.addAttribute("allParkings", parkingRepository.findAll());
        return "findparking";
    }

    @GetMapping("/parkingSpot/{ownerId}/{spotNumber}")
    public String parkingSpot(Model model, Authentication auth,
                              @PathVariable int ownerId, @PathVariable int spotNumber) {
        boolean loggedIn;
        loggedIn = auth != null;
        model.addAttribute("loggedIn", loggedIn);
        Parking parking = null;;
        ParkingSpot parkingSpot = null;

        List<Parking> allParkings = parkingRepository.findAll();
        List<ParkingSpot> allParkingSpots = parkingSpotRepository.findAll();

        for(int i = 0; i < allParkings.size(); i++) {
            if(allParkings.get(i).getId() == ownerId) parking = allParkings.get(i);
        }

        for(int i = 0; i < allParkingSpots.size(); i++) {
            if(allParkingSpots.get(i).getId().getParkingspotnumber() == spotNumber &&
                    allParkingSpots.get(i).getId().getUserid() == ownerId) parkingSpot = allParkingSpots.get(i);
        }
        System.out.println(parking);

        model.addAttribute("parking", parking);
        model.addAttribute("parkingSpot", parkingSpot);
        return "parkingspot";
    }

    @GetMapping("/search")
    public String searchResult(@RequestParam double x1, @RequestParam double y1,
                               @RequestParam double x2, @RequestParam double y2,
                               @RequestParam String type, @RequestParam int duration,
                               @RequestParam boolean error,
                               Model model, RedirectAttributes redirectAttributes) {
        if(error) {
            redirectAttributes.addFlashAttribute("noUserLocation", true);
        }

        Point destination = new Point(x1,y1);
        Point userLocation = new Point(x2, y2);
        redirectAttributes.addFlashAttribute("searchResult", false);
        WebClient webClient = WebClient.create("https://router.project-osrm.org");
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        System.out.println(webClient.get().uri("/route/v1/driving/43.2960669,17.0165161;43.25,17.01?geometries=geojson").retrieve();
//                .bodyToMono(String.class).block());

        List<Parking> availableParkings = parkingRepository.findAvailable(Instant.now(), duration);

        if(availableParkings.isEmpty()) {
            redirectAttributes.addFlashAttribute("noneAvailable", true);
            return "redirect:/findParking";
        }

//        if(allParkings.isEmpty()) {
//            return ""
//        }

        availableParkings.sort((x,y) -> {
            double p1x = x.getEntrancepointx().doubleValue();
            double p1y = x.getEntrancepointy().doubleValue();
            Double p1Dist = ((p1x - destination.getX()) * (p1x-destination.getX()) + (p1y-destination.getY()) * (p1y - destination.getY()));
            double p2x = y.getEntrancepointx().doubleValue();
            double p2y = y.getEntrancepointy().doubleValue();
            Double p2Dist = ((p2x - destination.getX()) * (p2x-destination.getX()) + (p2y-destination.getY()) * (p2y - destination.getY()));
            return p2Dist.compareTo(p1Dist);
        });

        Parking nearestParking = availableParkings.get(0);
        System.out.println(nearestParking);
        redirectAttributes.addFlashAttribute("nearestParking", nearestParking);
        List<ParkingSpot> spots = parkingSpotRepository.findAvailabeById(Instant.now(), duration, nearestParking.getId());
        spots.sort((x,y) -> {
            double p1x = (x.getPoint1x().doubleValue() + x.getPoint2x().doubleValue() + x.getPoint3x().doubleValue()
                    + x.getPoint3x().doubleValue() + x.getPoint4x().doubleValue())/4;
            double p1y = (x.getPoint1y().doubleValue() + x.getPoint2y().doubleValue() + x.getPoint3y().doubleValue()
                    + x.getPoint3y().doubleValue() + x.getPoint4y().doubleValue())/4;
            Double p1Dist = ((p1x - destination.getX()) * (p1x-destination.getX()) + (p1y-destination.getY()) * (p1y - destination.getY()));
            double p2x = (y.getPoint1x().doubleValue() + y.getPoint2x().doubleValue() + y.getPoint3x().doubleValue()
                    + y.getPoint3x().doubleValue() + y.getPoint4x().doubleValue())/4;
            double p2y = (y.getPoint1y().doubleValue() + y.getPoint2y().doubleValue() + y.getPoint3y().doubleValue()
                    + y.getPoint3y().doubleValue() + y.getPoint4y().doubleValue())/4;
            Double p2Dist = ((p2x - destination.getX()) * (p2x-destination.getX()) + (p2y-destination.getY()) * (p2y - destination.getY()));
            return p2Dist.compareTo(p1Dist);
        });

        if(error) {
            redirectAttributes.addFlashAttribute("searchResult", true);
            return "redirect:/findParking";
        }

        System.out.println(nearestParking);

        RoutingResponse response;
        try {
            response = objectMapper.readValue(webClient.get().uri("/route/v1/" + type + "/" + userLocation.getX()

                            + "," + userLocation.getY() + ";" + nearestParking.getEntrancepointx() + "," + nearestParking.getEntrancepointy()
//                            + "," + userLocation.getY() + ";" + destination.getX() + "," + destination.getY()
                            + "?geometries=geojson").retrieve()
                    .bodyToMono(String.class).block(), RoutingResponse.class);
        } catch (JsonProcessingException e) {
            redirectAttributes.addFlashAttribute("noRoute", true);
            return "redirect:/findParking";
        }

        if(response != null && response.getCode().equalsIgnoreCase("Ok") && !response.getRoutes().isEmpty()){
            redirectAttributes.addFlashAttribute("route", response.getRoutes().get(0).getGeometry().getCoordinates());
        } else {
            redirectAttributes.addFlashAttribute("noRoute", true);
        }



        redirectAttributes.addFlashAttribute("searchResult", true);
        return "redirect:/findParking";
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
