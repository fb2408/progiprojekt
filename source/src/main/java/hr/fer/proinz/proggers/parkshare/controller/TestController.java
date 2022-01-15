//package hr.fer.proinz.proggers.parkshare.controller;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import hr.fer.proinz.proggers.parkshare.dto.MessageDTO;
//import hr.fer.proinz.proggers.parkshare.dto.RegisterFormDTO;
//import hr.fer.proinz.proggers.parkshare.model.ClientReservation;
//import hr.fer.proinz.proggers.parkshare.model.ClientReservationId;
//import hr.fer.proinz.proggers.parkshare.model.osrm.RoutingResponse;
//import hr.fer.proinz.proggers.parkshare.repo.ClientReservationRepository;
//import hr.fer.proinz.proggers.parkshare.repo.ParkingRepository;
//import hr.fer.proinz.proggers.parkshare.repo.ParkingSpotRepository;
//import hr.fer.proinz.proggers.parkshare.repo.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.geo.Point;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.reactive.function.client.WebClient;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import java.time.Instant;
//import java.time.temporal.Temporal;
//import java.util.ArrayList;
//import java.util.List;
//
//@Controller
//@RequestMapping("/test")
//public class TestController {
//    @Autowired
//    UserRepository userRepo;
//    @Autowired
//    ParkingRepository parkingRepo;
//    @Autowired
//    ClientReservationRepository reservationRepo;
//    @Autowired
//    ParkingSpotRepository parkingSpotRepository;
//
//    @GetMapping
//    public String testMessageError(Model model) {
//        List<Point> markers = new ArrayList<>(List.of(new Point(0,0), new Point(1,1), new Point(1.5,1.5)));
//        model.addAttribute("markers", markers);
//        return "findparking";
//    }
//    @GetMapping("/testFindEmail")
//    public String testFindEmail(Model model){
//        System.out.println(userRepo.findByEmail("dcryophoenix@gmail.com"));
//        model.addAttribute("registerForm", new RegisterFormDTO());
//        return "index";
//    }
//
//    @GetMapping("/search")
//    public String testParam(@RequestParam double x,@RequestParam double y, Model model) {
//        System.out.println(x);
//        System.out.println(y);
//        return "findparking";
//    }
//
//    @GetMapping("/testOSRM")
//    public String testOSRM(Model model) {
//        WebClient webClient = WebClient.create("https://router.project-osrm.org");
//        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
////        System.out.println(webClient.get().uri("/route/v1/driving/43.2960669,17.0165161;43.25,17.01?geometries=geojson").retrieve();
////                .bodyToMono(String.class).block());
//        try {
//            RoutingResponse response = objectMapper.readValue(webClient.get().uri("/route/v1/driving/43.2960669,17.0165161;43.25,17.01?geometries=geojson").retrieve()
//                    .bodyToMono(String.class).block(), RoutingResponse.class);
//            System.out.println(response);
//        } catch (JsonProcessingException e) {
//            System.out.println(e.getMessage());
//        }
//        return "redirect:/";
//    }
//
//    @GetMapping("/testQuery")
//    public String testQ(){
//        Instant eleven = Instant.parse("2022-01-13T21:00:00.00Z");
//        System.out.println(eleven);
//        System.out.println(parkingRepo.findAvailable(eleven, 0));
//        System.out.println(parkingRepo.findAvailable(eleven, 1));
//        System.out.println(parkingRepo.findAvailable(eleven, 2));
//
//        System.out.println(reservationRepo.findAll());
//
//        System.out.println(parkingSpotRepository.findAvailabeById(Instant.now(), 3, 36));
//
//        return "redirect:/";
//    }
//
//    @GetMapping("/testRedirect1")
//    public String testRedirect1(RedirectAttributes redirectAttributes) {
//        redirectAttributes.addFlashAttribute("text", "Hello World!");
//        return "redirect:/test/testRedirect2";
//    }
//
//    @GetMapping("/testRedirect2")
//    public String testRedirect2(Model model) {
//        System.out.println(model.getAttribute("text"));
//        return "test";
//    }
//
//    @GetMapping("/testCalendar")
//    public String testRedirect2(@RequestParam(required = false) String date) {
//        System.out.println(date);
//        if(date != null) System.out.println(Instant.parse(date + ":00.00Z"));
//        return "test";
//    }
//}
