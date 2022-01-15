package hr.fer.proinz.proggers.parkshare.controller;

import hr.fer.proinz.proggers.parkshare.repo.ClientRepository;
import hr.fer.proinz.proggers.parkshare.repo.ClientReservationRepository;
import hr.fer.proinz.proggers.parkshare.repo.ParkingRepository;
import hr.fer.proinz.proggers.parkshare.repo.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {FindParkingController.class})
@ExtendWith(SpringExtension.class)
class FindParkingControllerTest {
    @MockBean
    private ClientRepository clientRepository;

    @MockBean
    private ClientReservationRepository clientReservationRepository;

    @Autowired
    private FindParkingController findParkingController;

    @MockBean
    private ParkingRepository parkingRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    void testAboutUs() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/about");
        MockMvcBuilders.standaloneSetup(this.findParkingController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("loggedIn"))
                .andExpect(MockMvcResultMatchers.view().name("aboutus"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("aboutus"));
    }

    @Test
    void testAboutUs2() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/about", null);
        MockMvcBuilders.standaloneSetup(this.findParkingController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("loggedIn"))
                .andExpect(MockMvcResultMatchers.view().name("aboutus"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("aboutus"));
    }
}

