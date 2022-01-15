package hr.fer.proinz.proggers.parkshare.controller;

import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doNothing;

import hr.fer.proinz.proggers.parkshare.repo.ParkingOwnerRepository;
import hr.fer.proinz.proggers.parkshare.repo.UserRepository;
import hr.fer.proinz.proggers.parkshare.service.UserService;
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

@ContextConfiguration(classes = {AdminController.class})
@ExtendWith(SpringExtension.class)
class AdminControllerTest {
    @Autowired
    private AdminController adminController;

    @MockBean
    private ParkingOwnerRepository parkingOwnerRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @Test
    void testAdminConfirmOwner() throws Exception {
        doNothing().when(this.userService).confirmOwner(anyInt());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/admin/confirmOwner/{id}", 1);
        MockMvcBuilders.standaloneSetup(this.adminController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/admin?ownerConfirmed=true"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin?ownerConfirmed=true"));
    }
}

