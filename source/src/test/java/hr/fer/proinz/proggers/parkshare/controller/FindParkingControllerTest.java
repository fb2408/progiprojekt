package hr.fer.proinz.proggers.parkshare.controller;

import hr.fer.proinz.proggers.parkshare.repo.ClientRepository;
import hr.fer.proinz.proggers.parkshare.repo.ClientReservationRepository;
import hr.fer.proinz.proggers.parkshare.repo.ParkingRepository;
import hr.fer.proinz.proggers.parkshare.repo.UserRepository;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.intercept.RunAsUserToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {FindParkingController.class})
@ExtendWith(SpringExtension.class)
class FindParkingControllerTest {

    @Autowired
    private FindParkingController findParkingController;

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
    void testReserveSpot() throws Exception {
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/findParking");
        MockHttpServletRequestBuilder paramResult = postResult.param("duration", String.valueOf(1));
        MockHttpServletRequestBuilder paramResult1 = paramResult.param("ownerUserId", String.valueOf(1));
        MockHttpServletRequestBuilder paramResult2 = paramResult1.param("parkingSpotNumber", String.valueOf(1));
        MockHttpServletRequestBuilder paramResult3 = paramResult2.param("payNow", String.valueOf(true));
        MockHttpServletRequestBuilder paramResult4 = paramResult3.param("recurring", String.valueOf(true));
        MockHttpServletRequestBuilder requestBuilder = paramResult4.param("timeOfStart", String.valueOf((Object) null));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.findParkingController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    void testShowMap() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/findParking");
        MockMvcBuilders.standaloneSetup(this.findParkingController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("loggedIn"))
                .andExpect(MockMvcResultMatchers.view().name("findparking"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("findparking"));
    }

    @Test
    void testShowMap2() throws Exception {
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/findParking");
        getResult.characterEncoding("Encoding");
        MockMvcBuilders.standaloneSetup(this.findParkingController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("loggedIn"))
                .andExpect(MockMvcResultMatchers.view().name("findparking"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("findparking"));
    }

    @Test
    void testShowMap3() throws Exception {
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/findParking");
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        getResult.principal(new RunAsUserToken("Key", "Principal", "Credentials", authorities, Authentication.class));
        MockMvcBuilders.standaloneSetup(this.findParkingController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("loggedIn"))
                .andExpect(MockMvcResultMatchers.view().name("findparking"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("findparking"));
    }
}

