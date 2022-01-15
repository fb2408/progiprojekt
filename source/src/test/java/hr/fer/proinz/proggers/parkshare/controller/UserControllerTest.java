package hr.fer.proinz.proggers.parkshare.controller;

import hr.fer.proinz.proggers.parkshare.repo.ClientRepository;
import hr.fer.proinz.proggers.parkshare.repo.ParkingOwnerRepository;
import hr.fer.proinz.proggers.parkshare.repo.ParkingRepository;
import hr.fer.proinz.proggers.parkshare.repo.ParkingSpotOccupancyRepository;
import hr.fer.proinz.proggers.parkshare.repo.ParkingSpotRepository;
import hr.fer.proinz.proggers.parkshare.repo.UserRepository;
import hr.fer.proinz.proggers.parkshare.service.UserService;

import java.math.BigDecimal;

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

@ContextConfiguration(classes = {UserController.class})
@ExtendWith(SpringExtension.class)
class UserControllerTest {
    @MockBean
    private ClientRepository clientRepository;

    @MockBean
    private ParkingOwnerRepository parkingOwnerRepository;

    @MockBean
    private ParkingRepository parkingRepository;

    @MockBean
    private ParkingSpotOccupancyRepository parkingSpotOccupancyRepository;

    @MockBean
    private ParkingSpotRepository parkingSpotRepository;

    @Autowired
    private UserController userController;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @Test
    void testChargeAccount() throws Exception {
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/profile/ChargeAccount");
        MockHttpServletRequestBuilder requestBuilder = postResult.param("amount", String.valueOf(BigDecimal.valueOf(42L)));
        MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/loginRouter"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/loginRouter"));
    }

    @Test
    void testChargeAccount2() throws Exception {
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/profile/ChargeAccount", null);
        MockHttpServletRequestBuilder requestBuilder = postResult.param("amount", String.valueOf(BigDecimal.valueOf(42L)));
        MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/loginRouter"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/loginRouter"));
    }
}

