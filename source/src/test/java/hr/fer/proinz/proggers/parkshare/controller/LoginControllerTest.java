package hr.fer.proinz.proggers.parkshare.controller;

import com.sun.security.auth.UserPrincipal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {LoginController.class})
@ExtendWith(SpringExtension.class)
class LoginControllerTest {
    @Autowired
    private LoginController loginController;

    @Test
    void testRedirectToLandingPage() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/loginRouter");
        MockMvcBuilders.standaloneSetup(this.loginController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));
    }

    @Test
    void testRedirectToLandingPage2() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/loginRouter", null);
        MockMvcBuilders.standaloneSetup(this.loginController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));
    }

    @Test
    void testRedirectToLandingPage3() throws Exception {
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/loginRouter");
        getResult.principal(new UserPrincipal("principal"));
        MockMvcBuilders.standaloneSetup(this.loginController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/profile"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/profile"));
    }
}

