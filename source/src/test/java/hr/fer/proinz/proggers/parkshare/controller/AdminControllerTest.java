package hr.fer.proinz.proggers.parkshare.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import hr.fer.proinz.proggers.parkshare.dto.MessageDTO;
import hr.fer.proinz.proggers.parkshare.dto.UserDTO;
import hr.fer.proinz.proggers.parkshare.model.UserModel;
import hr.fer.proinz.proggers.parkshare.repo.ClientRepository;
import hr.fer.proinz.proggers.parkshare.repo.ParkingOwnerRepository;
import hr.fer.proinz.proggers.parkshare.repo.ParkingSpotRepository;
import hr.fer.proinz.proggers.parkshare.repo.UserRepository;
import hr.fer.proinz.proggers.parkshare.service.EmailService;
import hr.fer.proinz.proggers.parkshare.service.UserService;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import java.util.NoSuchElementException;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ModelMap;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

@ContextConfiguration(classes = {AdminController.class})
@ExtendWith(SpringExtension.class)
class AdminControllerTest {
    @Autowired
    private AdminController adminController;

    @MockBean
    private ParkingOwnerRepository parkingOwnerRepository;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void testEditAdminUserDetails() {
        UserModel userModel = new UserModel();
        userModel.setConfirmed(true);
        userModel.setEmail("jane.doe@example.org");
        userModel.setFirstName("Jane");
        userModel.setId(1);
        userModel.setName("Name");
        userModel.setSurname("Doe");
        userModel.setTempPassword("iloveyou");
        userModel.setType("Type");
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.getById((Integer) any())).thenThrow(new InvalidParameterException("Msg"));
        when(userRepository.findByName((String) any())).thenReturn(userModel);
        when(userRepository.existsByName((String) any())).thenReturn(true);
        ClientRepository clientRepository = mock(ClientRepository.class);
        EmailService emailService = new EmailService(new JavaMailSenderImpl());
        ParkingSpotRepository parkingSpotRepository = mock(ParkingSpotRepository.class);
        ParkingOwnerRepository parkingOwnerRepository = mock(ParkingOwnerRepository.class);
        UserService userService = new UserService(clientRepository, emailService, userRepository, parkingSpotRepository,
                parkingOwnerRepository, new BCryptPasswordEncoder());

        UserModel userModel1 = new UserModel();
        userModel1.setConfirmed(true);
        userModel1.setEmail("jane.doe@example.org");
        userModel1.setFirstName("Jane");
        userModel1.setId(1);
        userModel1.setName("Name");
        userModel1.setSurname("Doe");
        userModel1.setTempPassword("iloveyou");
        userModel1.setType("Type");
        UserRepository userRepository1 = mock(UserRepository.class);
        when(userRepository1.findByEmail((String) any())).thenReturn(userModel1);
        AdminController adminController = new AdminController(userService, userRepository1,
                mock(ParkingOwnerRepository.class));
        UserDTO updatedUser = new UserDTO();
        ModelMap model = new ModelMap();
        ModelAndView actualEditAdminUserDetailsResult = adminController.editAdminUserDetails(updatedUser, model,
                new TestingAuthenticationToken("Principal", "Credentials"));
        assertTrue(actualEditAdminUserDetailsResult.isReference());
        Map<String, Object> model1 = actualEditAdminUserDetailsResult.getModel();
        assertSame(model1, actualEditAdminUserDetailsResult.getModelMap());
        Object getResult = model1.get("errors");
        assertEquals(1, ((Collection<MessageDTO>) getResult).size());
        assertNull(((UserDTO) model1.get("user")).getIban());
        assertEquals("iloveyou", ((UserDTO) model1.get("user")).getPassword());
        assertTrue(((UserDTO) model1.get("user")).isConfirmed());
        assertFalse(((UserDTO) model1.get("user")).isClient());
        assertNull(((UserDTO) model1.get("user")).getWalletBalance());
        assertEquals("Name", ((UserDTO) model1.get("user")).getUsername());
        assertEquals("Jane", ((UserDTO) model1.get("user")).getUserfirstname());
        assertEquals("jane.doe@example.org", ((UserDTO) model1.get("user")).getMail());
        assertEquals("Doe", ((UserDTO) model1.get("user")).getLastName());
        assertEquals(1, ((UserDTO) model1.get("user")).getId().intValue());
        MessageDTO getResult1 = ((List<MessageDTO>) getResult).get(0);
        assertEquals("Update failed!", getResult1.getMsg());
        assertEquals("Old password isn't correct.", getResult1.getDesc());
        verify(userRepository).existsByName((String) any());
        verify(userRepository).findByName((String) any());
        verify(userRepository).getById((Integer) any());
        verify(userRepository1, atLeast(1)).findByEmail((String) any());
    }

    @Test
    void testEditAdminUserDetails2() {
        UserModel userModel = new UserModel();
        userModel.setConfirmed(true);
        userModel.setEmail("jane.doe@example.org");
        userModel.setFirstName("Jane");
        userModel.setId(1);
        userModel.setName("Name");
        userModel.setSurname("Doe");
        userModel.setTempPassword("iloveyou");
        userModel.setType("Type");
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.getById((Integer) any())).thenThrow(new ResponseStatusException(HttpStatus.CONTINUE));
        when(userRepository.findByName((String) any())).thenReturn(userModel);
        when(userRepository.existsByName((String) any())).thenReturn(true);
        ClientRepository clientRepository = mock(ClientRepository.class);
        EmailService emailService = new EmailService(new JavaMailSenderImpl());
        ParkingSpotRepository parkingSpotRepository = mock(ParkingSpotRepository.class);
        ParkingOwnerRepository parkingOwnerRepository = mock(ParkingOwnerRepository.class);
        UserService userService = new UserService(clientRepository, emailService, userRepository, parkingSpotRepository,
                parkingOwnerRepository, new BCryptPasswordEncoder());

        UserModel userModel1 = new UserModel();
        userModel1.setConfirmed(true);
        userModel1.setEmail("jane.doe@example.org");
        userModel1.setFirstName("Jane");
        userModel1.setId(1);
        userModel1.setName("Name");
        userModel1.setSurname("Doe");
        userModel1.setTempPassword("iloveyou");
        userModel1.setType("Type");
        UserRepository userRepository1 = mock(UserRepository.class);
        when(userRepository1.findByEmail((String) any())).thenReturn(userModel1);
        AdminController adminController = new AdminController(userService, userRepository1,
                mock(ParkingOwnerRepository.class));
        UserDTO updatedUser = new UserDTO();
        ModelMap model = new ModelMap();
        ModelAndView actualEditAdminUserDetailsResult = adminController.editAdminUserDetails(updatedUser, model,
                new TestingAuthenticationToken("Principal", "Credentials"));
        assertTrue(actualEditAdminUserDetailsResult.isReference());
        Map<String, Object> model1 = actualEditAdminUserDetailsResult.getModel();
        assertSame(model1, actualEditAdminUserDetailsResult.getModelMap());
        Object getResult = model1.get("errors");
        assertEquals(1, ((Collection<MessageDTO>) getResult).size());
        assertNull(((UserDTO) model1.get("user")).getIban());
        assertEquals("iloveyou", ((UserDTO) model1.get("user")).getPassword());
        assertTrue(((UserDTO) model1.get("user")).isConfirmed());
        assertFalse(((UserDTO) model1.get("user")).isClient());
        assertNull(((UserDTO) model1.get("user")).getWalletBalance());
        assertEquals("Name", ((UserDTO) model1.get("user")).getUsername());
        assertEquals("Jane", ((UserDTO) model1.get("user")).getUserfirstname());
        assertEquals("jane.doe@example.org", ((UserDTO) model1.get("user")).getMail());
        assertEquals("Doe", ((UserDTO) model1.get("user")).getLastName());
        assertEquals(1, ((UserDTO) model1.get("user")).getId().intValue());
        MessageDTO getResult1 = ((List<MessageDTO>) getResult).get(0);
        assertEquals("Update failed!", getResult1.getMsg());
        assertEquals("Account with given username already exists.", getResult1.getDesc());
        verify(userRepository).existsByName((String) any());
        verify(userRepository).findByName((String) any());
        verify(userRepository).getById((Integer) any());
        verify(userRepository1, atLeast(1)).findByEmail((String) any());
    }

    @Test
    void testEditAdminUserDetails3() {
        UserModel userModel = new UserModel();
        userModel.setConfirmed(true);
        userModel.setEmail("jane.doe@example.org");
        userModel.setFirstName("Jane");
        userModel.setId(123);
        userModel.setName("Name");
        userModel.setSurname("Doe");
        userModel.setTempPassword("iloveyou");
        userModel.setType("Type");

        UserModel userModel1 = new UserModel();
        userModel1.setConfirmed(true);
        userModel1.setEmail("jane.doe@example.org");
        userModel1.setFirstName("Jane");
        userModel1.setId(1);
        userModel1.setName("Name");
        userModel1.setSurname("Doe");
        userModel1.setTempPassword("iloveyou");
        userModel1.setType("Type");
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.getById((Integer) any())).thenReturn(userModel1);
        when(userRepository.findByName((String) any())).thenReturn(userModel);
        when(userRepository.existsByName((String) any())).thenReturn(true);
        ClientRepository clientRepository = mock(ClientRepository.class);
        EmailService emailService = new EmailService(new JavaMailSenderImpl());
        ParkingSpotRepository parkingSpotRepository = mock(ParkingSpotRepository.class);
        ParkingOwnerRepository parkingOwnerRepository = mock(ParkingOwnerRepository.class);
        UserService userService = new UserService(clientRepository, emailService, userRepository, parkingSpotRepository,
                parkingOwnerRepository, new BCryptPasswordEncoder());

        UserModel userModel2 = new UserModel();
        userModel2.setConfirmed(true);
        userModel2.setEmail("jane.doe@example.org");
        userModel2.setFirstName("Jane");
        userModel2.setId(1);
        userModel2.setName("Name");
        userModel2.setSurname("Doe");
        userModel2.setTempPassword("iloveyou");
        userModel2.setType("Type");
        UserRepository userRepository1 = mock(UserRepository.class);
        when(userRepository1.findByEmail((String) any())).thenReturn(userModel2);
        AdminController adminController = new AdminController(userService, userRepository1,
                mock(ParkingOwnerRepository.class));
        UserDTO updatedUser = new UserDTO();
        ModelMap model = new ModelMap();
        ModelAndView actualEditAdminUserDetailsResult = adminController.editAdminUserDetails(updatedUser, model,
                new TestingAuthenticationToken("Principal", "Credentials"));
        assertTrue(actualEditAdminUserDetailsResult.isReference());
        Map<String, Object> model1 = actualEditAdminUserDetailsResult.getModel();
        assertSame(model1, actualEditAdminUserDetailsResult.getModelMap());
        Object getResult = model1.get("errors");
        assertEquals(1, ((Collection<MessageDTO>) getResult).size());
        assertNull(((UserDTO) model1.get("user")).getIban());
        assertEquals("iloveyou", ((UserDTO) model1.get("user")).getPassword());
        assertTrue(((UserDTO) model1.get("user")).isConfirmed());
        assertFalse(((UserDTO) model1.get("user")).isClient());
        assertNull(((UserDTO) model1.get("user")).getWalletBalance());
        assertEquals("Name", ((UserDTO) model1.get("user")).getUsername());
        assertEquals("Jane", ((UserDTO) model1.get("user")).getUserfirstname());
        assertEquals("jane.doe@example.org", ((UserDTO) model1.get("user")).getMail());
        assertEquals("Doe", ((UserDTO) model1.get("user")).getLastName());
        assertEquals(1, ((UserDTO) model1.get("user")).getId().intValue());
        MessageDTO getResult1 = ((List<MessageDTO>) getResult).get(0);
        assertEquals("Update failed!", getResult1.getMsg());
        assertEquals("Account with given username already exists.", getResult1.getDesc());
        verify(userRepository).existsByName((String) any());
        verify(userRepository).findByName((String) any());
        verify(userRepository1, atLeast(1)).findByEmail((String) any());
    }

    @Test
    void testEditAdminUserDetails4() {
        UserModel userModel = new UserModel();
        userModel.setConfirmed(true);
        userModel.setEmail("jane.doe@example.org");
        userModel.setFirstName("Jane");
        userModel.setId(1);
        userModel.setName("Name");
        userModel.setSurname("Doe");
        userModel.setTempPassword("iloveyou");
        userModel.setType("Type");
        UserService userService = mock(UserService.class);
        when(userService.updateUser((UserDTO) any(), anyBoolean(), anyInt())).thenReturn(userModel);

        UserModel userModel1 = new UserModel();
        userModel1.setConfirmed(true);
        userModel1.setEmail("jane.doe@example.org");
        userModel1.setFirstName("Jane");
        userModel1.setId(1);
        userModel1.setName("Name");
        userModel1.setSurname("Doe");
        userModel1.setTempPassword("iloveyou");
        userModel1.setType("Type");
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByEmail((String) any())).thenReturn(userModel1);
        AdminController adminController = new AdminController(userService, userRepository,
                mock(ParkingOwnerRepository.class));
        UserDTO updatedUser = new UserDTO();
        ModelMap model = new ModelMap();
        ModelAndView actualEditAdminUserDetailsResult = adminController.editAdminUserDetails(updatedUser, model,
                new TestingAuthenticationToken("Principal", "Credentials"));
        assertTrue(actualEditAdminUserDetailsResult.isReference());
        Map<String, Object> model1 = actualEditAdminUserDetailsResult.getModel();
        assertSame(model1, actualEditAdminUserDetailsResult.getModelMap());
        Object getResult = model1.get("information");
        assertEquals(1, ((Collection<MessageDTO>) getResult).size());
        MessageDTO getResult1 = ((List<MessageDTO>) getResult).get(0);
        assertEquals("Your data has been updated.", getResult1.getDesc());
        assertEquals("Success!", getResult1.getMsg());
        verify(userService).updateUser((UserDTO) any(), anyBoolean(), anyInt());
        verify(userRepository).findByEmail((String) any());
    }

    @Test
    void testEditAdminUserDetails5() {
        UserService userService = mock(UserService.class);
        when(userService.UserToDTO((UserModel) any())).thenReturn(new UserDTO());
        when(userService.updateUser((UserDTO) any(), anyBoolean(), anyInt()))
                .thenThrow(new InvalidParameterException("Msg"));

        UserModel userModel = new UserModel();
        userModel.setConfirmed(true);
        userModel.setEmail("jane.doe@example.org");
        userModel.setFirstName("Jane");
        userModel.setId(1);
        userModel.setName("Name");
        userModel.setSurname("Doe");
        userModel.setTempPassword("iloveyou");
        userModel.setType("Type");
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByEmail((String) any())).thenReturn(userModel);
        AdminController adminController = new AdminController(userService, userRepository,
                mock(ParkingOwnerRepository.class));
        UserDTO updatedUser = new UserDTO();
        ModelMap model = new ModelMap();
        ModelAndView actualEditAdminUserDetailsResult = adminController.editAdminUserDetails(updatedUser, model,
                new TestingAuthenticationToken("Principal", "Credentials"));
        assertTrue(actualEditAdminUserDetailsResult.isReference());
        Map<String, Object> model1 = actualEditAdminUserDetailsResult.getModel();
        assertSame(model1, actualEditAdminUserDetailsResult.getModelMap());
        Object getResult = model1.get("errors");
        assertEquals(1, ((Collection<MessageDTO>) getResult).size());
        MessageDTO getResult1 = ((List<MessageDTO>) getResult).get(0);
        assertEquals("Old password isn't correct.", getResult1.getDesc());
        assertEquals("Update failed!", getResult1.getMsg());
        verify(userService).UserToDTO((UserModel) any());
        verify(userService).updateUser((UserDTO) any(), anyBoolean(), anyInt());
        verify(userRepository, atLeast(1)).findByEmail((String) any());
    }

    @Test
    void testEditAdminUserDetails6() {
        UserService userService = mock(UserService.class);
        when(userService.UserToDTO((UserModel) any())).thenThrow(new InvalidParameterException("Update failed!"));
        when(userService.updateUser((UserDTO) any(), anyBoolean(), anyInt()))
                .thenThrow(new InvalidParameterException("Msg"));

        UserModel userModel = new UserModel();
        userModel.setConfirmed(true);
        userModel.setEmail("jane.doe@example.org");
        userModel.setFirstName("Jane");
        userModel.setId(1);
        userModel.setName("Name");
        userModel.setSurname("Doe");
        userModel.setTempPassword("iloveyou");
        userModel.setType("Type");
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByEmail((String) any())).thenReturn(userModel);
        AdminController adminController = new AdminController(userService, userRepository,
                mock(ParkingOwnerRepository.class));
        UserDTO updatedUser = new UserDTO();
        ModelMap model = new ModelMap();
        assertThrows(InvalidParameterException.class, () -> adminController.editAdminUserDetails(updatedUser, model,
                new TestingAuthenticationToken("Principal", "Credentials")));
        verify(userService).UserToDTO((UserModel) any());
        verify(userService).updateUser((UserDTO) any(), anyBoolean(), anyInt());
        verify(userRepository, atLeast(1)).findByEmail((String) any());
    }

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

    @Test
    void testAdminEditUserDetails() {
        UserModel userModel = new UserModel();
        userModel.setConfirmed(true);
        userModel.setEmail("jane.doe@example.org");
        userModel.setFirstName("Jane");
        userModel.setId(1);
        userModel.setName("Name");
        userModel.setSurname("Doe");
        userModel.setTempPassword("iloveyou");
        userModel.setType("Type");

        UserModel userModel1 = new UserModel();
        userModel1.setConfirmed(true);
        userModel1.setEmail("jane.doe@example.org");
        userModel1.setFirstName("Jane");
        userModel1.setId(1);
        userModel1.setName("Name");
        userModel1.setSurname("Doe");
        userModel1.setTempPassword("iloveyou");
        userModel1.setType("Type");

        UserModel userModel2 = new UserModel();
        userModel2.setConfirmed(true);
        userModel2.setEmail("jane.doe@example.org");
        userModel2.setFirstName("Jane");
        userModel2.setId(1);
        userModel2.setName("Name");
        userModel2.setSurname("Doe");
        userModel2.setTempPassword("iloveyou");
        userModel2.setType("Type");
        Optional<UserModel> ofResult = Optional.of(userModel2);

        UserModel userModel3 = new UserModel();
        userModel3.setConfirmed(true);
        userModel3.setEmail("jane.doe@example.org");
        userModel3.setFirstName("Jane");
        userModel3.setId(1);
        userModel3.setName("Name");
        userModel3.setSurname("Doe");
        userModel3.setTempPassword("iloveyou");
        userModel3.setType("Type");
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.save((UserModel) any())).thenReturn(userModel3);
        when(userRepository.findById((Integer) any())).thenReturn(ofResult);
        when(userRepository.getById((Integer) any())).thenReturn(userModel1);
        when(userRepository.findByName((String) any())).thenReturn(userModel);
        when(userRepository.existsByName((String) any())).thenReturn(true);
        ClientRepository clientRepository = mock(ClientRepository.class);
        EmailService emailService = new EmailService(new JavaMailSenderImpl());
        ParkingSpotRepository parkingSpotRepository = mock(ParkingSpotRepository.class);
        ParkingOwnerRepository parkingOwnerRepository = mock(ParkingOwnerRepository.class);
        AdminController adminController = new AdminController(new UserService(clientRepository, emailService,
                userRepository, parkingSpotRepository, parkingOwnerRepository, new BCryptPasswordEncoder()),
                mock(UserRepository.class), mock(ParkingOwnerRepository.class));

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        assertEquals("redirect:/admin?dataUpdated=true", adminController.adminEditUserDetails(userDTO));
        verify(userRepository).existsByName((String) any());
        verify(userRepository).findById((Integer) any());
        verify(userRepository).findByName((String) any());
        verify(userRepository).getById((Integer) any());
        verify(userRepository).save((UserModel) any());
        assertTrue(adminController.userService.getAllUnconfirmedOwners().isEmpty());
    }

    @Test
    void testAdminEditUserDetails2() {
        UserModel userModel = new UserModel();
        userModel.setConfirmed(true);
        userModel.setEmail("jane.doe@example.org");
        userModel.setFirstName("Jane");
        userModel.setId(1);
        userModel.setName("Name");
        userModel.setSurname("Doe");
        userModel.setTempPassword("iloveyou");
        userModel.setType("Type");

        UserModel userModel1 = new UserModel();
        userModel1.setConfirmed(true);
        userModel1.setEmail("jane.doe@example.org");
        userModel1.setFirstName("Jane");
        userModel1.setId(1);
        userModel1.setName("Name");
        userModel1.setSurname("Doe");
        userModel1.setTempPassword("iloveyou");
        userModel1.setType("Type");

        UserModel userModel2 = new UserModel();
        userModel2.setConfirmed(true);
        userModel2.setEmail("jane.doe@example.org");
        userModel2.setFirstName("Jane");
        userModel2.setId(1);
        userModel2.setName("Name");
        userModel2.setSurname("Doe");
        userModel2.setTempPassword("iloveyou");
        userModel2.setType("Type");
        Optional<UserModel> ofResult = Optional.of(userModel2);
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.save((UserModel) any())).thenThrow(new ResponseStatusException(HttpStatus.CONTINUE));
        when(userRepository.findById((Integer) any())).thenReturn(ofResult);
        when(userRepository.getById((Integer) any())).thenReturn(userModel1);
        when(userRepository.findByName((String) any())).thenReturn(userModel);
        when(userRepository.existsByName((String) any())).thenReturn(true);
        ClientRepository clientRepository = mock(ClientRepository.class);
        EmailService emailService = new EmailService(new JavaMailSenderImpl());
        ParkingSpotRepository parkingSpotRepository = mock(ParkingSpotRepository.class);
        ParkingOwnerRepository parkingOwnerRepository = mock(ParkingOwnerRepository.class);
        AdminController adminController = new AdminController(new UserService(clientRepository, emailService,
                userRepository, parkingSpotRepository, parkingOwnerRepository, new BCryptPasswordEncoder()),
                mock(UserRepository.class), mock(ParkingOwnerRepository.class));

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        assertEquals("redirect:/admin?userWithGivenUsernameExists=true", adminController.adminEditUserDetails(userDTO));
        verify(userRepository).existsByName((String) any());
        verify(userRepository).findById((Integer) any());
        verify(userRepository).findByName((String) any());
        verify(userRepository).getById((Integer) any());
        verify(userRepository).save((UserModel) any());
        assertTrue(adminController.userService.getAllUnconfirmedOwners().isEmpty());
    }

    @Test
    void testAdminEditUserDetails3() {
        UserModel userModel = new UserModel();
        userModel.setConfirmed(true);
        userModel.setEmail("jane.doe@example.org");
        userModel.setFirstName("Jane");
        userModel.setId(1);
        userModel.setName("Name");
        userModel.setSurname("Doe");
        userModel.setTempPassword("iloveyou");
        userModel.setType("Type");

        UserModel userModel1 = new UserModel();
        userModel1.setConfirmed(true);
        userModel1.setEmail("jane.doe@example.org");
        userModel1.setFirstName("Jane");
        userModel1.setId(1);
        userModel1.setName("Name");
        userModel1.setSurname("Doe");
        userModel1.setTempPassword("iloveyou");
        userModel1.setType("Type");

        UserModel userModel2 = new UserModel();
        userModel2.setConfirmed(true);
        userModel2.setEmail("jane.doe@example.org");
        userModel2.setFirstName("Jane");
        userModel2.setId(1);
        userModel2.setName("Name");
        userModel2.setSurname("Doe");
        userModel2.setTempPassword("iloveyou");
        userModel2.setType("Type");
        Optional<UserModel> ofResult = Optional.of(userModel2);
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.save((UserModel) any())).thenThrow(new NoSuchElementException(""));
        when(userRepository.findById((Integer) any())).thenReturn(ofResult);
        when(userRepository.getById((Integer) any())).thenReturn(userModel1);
        when(userRepository.findByName((String) any())).thenReturn(userModel);
        when(userRepository.existsByName((String) any())).thenReturn(true);
        ClientRepository clientRepository = mock(ClientRepository.class);
        EmailService emailService = new EmailService(new JavaMailSenderImpl());
        ParkingSpotRepository parkingSpotRepository = mock(ParkingSpotRepository.class);
        ParkingOwnerRepository parkingOwnerRepository = mock(ParkingOwnerRepository.class);
        AdminController adminController = new AdminController(new UserService(clientRepository, emailService,
                userRepository, parkingSpotRepository, parkingOwnerRepository, new BCryptPasswordEncoder()),
                mock(UserRepository.class), mock(ParkingOwnerRepository.class));

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        assertEquals("redirect:/admin?invalidId=true", adminController.adminEditUserDetails(userDTO));
        verify(userRepository).existsByName((String) any());
        verify(userRepository).findById((Integer) any());
        verify(userRepository).findByName((String) any());
        verify(userRepository).getById((Integer) any());
        verify(userRepository).save((UserModel) any());
        assertTrue(adminController.userService.getAllUnconfirmedOwners().isEmpty());
    }

    @Test
    void testAdminEditUserDetails4() {
        UserModel userModel = new UserModel();
        userModel.setConfirmed(true);
        userModel.setEmail("jane.doe@example.org");
        userModel.setFirstName("Jane");
        userModel.setId(1);
        userModel.setName("Name");
        userModel.setSurname("Doe");
        userModel.setTempPassword("iloveyou");
        userModel.setType("Type");

        UserModel userModel1 = new UserModel();
        userModel1.setConfirmed(true);
        userModel1.setEmail("jane.doe@example.org");
        userModel1.setFirstName("Jane");
        userModel1.setId(1);
        userModel1.setName("Name");
        userModel1.setSurname("Doe");
        userModel1.setTempPassword("iloveyou");
        userModel1.setType("Type");

        UserModel userModel2 = new UserModel();
        userModel2.setConfirmed(true);
        userModel2.setEmail("jane.doe@example.org");
        userModel2.setFirstName("Jane");
        userModel2.setId(1);
        userModel2.setName("Name");
        userModel2.setSurname("Doe");
        userModel2.setTempPassword("iloveyou");
        userModel2.setType("Type");
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.save((UserModel) any())).thenReturn(userModel2);
        when(userRepository.findById((Integer) any())).thenReturn(Optional.empty());
        when(userRepository.getById((Integer) any())).thenReturn(userModel1);
        when(userRepository.findByName((String) any())).thenReturn(userModel);
        when(userRepository.existsByName((String) any())).thenReturn(true);
        ClientRepository clientRepository = mock(ClientRepository.class);
        EmailService emailService = new EmailService(new JavaMailSenderImpl());
        ParkingSpotRepository parkingSpotRepository = mock(ParkingSpotRepository.class);
        ParkingOwnerRepository parkingOwnerRepository = mock(ParkingOwnerRepository.class);
        AdminController adminController = new AdminController(new UserService(clientRepository, emailService,
                userRepository, parkingSpotRepository, parkingOwnerRepository, new BCryptPasswordEncoder()),
                mock(UserRepository.class), mock(ParkingOwnerRepository.class));

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        assertEquals("redirect:/admin?invalidId=true", adminController.adminEditUserDetails(userDTO));
        verify(userRepository).existsByName((String) any());
        verify(userRepository).findById((Integer) any());
        verify(userRepository).findByName((String) any());
        verify(userRepository).getById((Integer) any());
        assertTrue(adminController.userService.getAllUnconfirmedOwners().isEmpty());
    }

    @Test
    void testAdminEditUserDetails5() {
        UserModel userModel = new UserModel();
        userModel.setConfirmed(true);
        userModel.setEmail("jane.doe@example.org");
        userModel.setFirstName("Jane");
        userModel.setId(123);
        userModel.setName("Name");
        userModel.setSurname("Doe");
        userModel.setTempPassword("iloveyou");
        userModel.setType("Type");

        UserModel userModel1 = new UserModel();
        userModel1.setConfirmed(true);
        userModel1.setEmail("jane.doe@example.org");
        userModel1.setFirstName("Jane");
        userModel1.setId(1);
        userModel1.setName("Name");
        userModel1.setSurname("Doe");
        userModel1.setTempPassword("iloveyou");
        userModel1.setType("Type");

        UserModel userModel2 = new UserModel();
        userModel2.setConfirmed(true);
        userModel2.setEmail("jane.doe@example.org");
        userModel2.setFirstName("Jane");
        userModel2.setId(1);
        userModel2.setName("Name");
        userModel2.setSurname("Doe");
        userModel2.setTempPassword("iloveyou");
        userModel2.setType("Type");
        Optional<UserModel> ofResult = Optional.of(userModel2);

        UserModel userModel3 = new UserModel();
        userModel3.setConfirmed(true);
        userModel3.setEmail("jane.doe@example.org");
        userModel3.setFirstName("Jane");
        userModel3.setId(1);
        userModel3.setName("Name");
        userModel3.setSurname("Doe");
        userModel3.setTempPassword("iloveyou");
        userModel3.setType("Type");
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.save((UserModel) any())).thenReturn(userModel3);
        when(userRepository.findById((Integer) any())).thenReturn(ofResult);
        when(userRepository.getById((Integer) any())).thenReturn(userModel1);
        when(userRepository.findByName((String) any())).thenReturn(userModel);
        when(userRepository.existsByName((String) any())).thenReturn(true);
        ClientRepository clientRepository = mock(ClientRepository.class);
        EmailService emailService = new EmailService(new JavaMailSenderImpl());
        ParkingSpotRepository parkingSpotRepository = mock(ParkingSpotRepository.class);
        ParkingOwnerRepository parkingOwnerRepository = mock(ParkingOwnerRepository.class);
        AdminController adminController = new AdminController(new UserService(clientRepository, emailService,
                userRepository, parkingSpotRepository, parkingOwnerRepository, new BCryptPasswordEncoder()),
                mock(UserRepository.class), mock(ParkingOwnerRepository.class));

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        assertEquals("redirect:/admin?userWithGivenUsernameExists=true", adminController.adminEditUserDetails(userDTO));
        verify(userRepository).existsByName((String) any());
        verify(userRepository).findByName((String) any());
        assertTrue(adminController.userService.getAllUnconfirmedOwners().isEmpty());
    }

    @Test
    void testAdminEditUserDetails6() {
        UserModel userModel = new UserModel();
        userModel.setConfirmed(true);
        userModel.setEmail("jane.doe@example.org");
        userModel.setFirstName("Jane");
        userModel.setId(1);
        userModel.setName("Name");
        userModel.setSurname("Doe");
        userModel.setTempPassword("iloveyou");
        userModel.setType("Type");

        UserModel userModel1 = new UserModel();
        userModel1.setConfirmed(true);
        userModel1.setEmail("jane.doe@example.org");
        userModel1.setFirstName("Jane");
        userModel1.setId(1);
        userModel1.setName("Name");
        userModel1.setSurname("Doe");
        userModel1.setTempPassword("iloveyou");
        userModel1.setType("Type");

        UserModel userModel2 = new UserModel();
        userModel2.setConfirmed(true);
        userModel2.setEmail("jane.doe@example.org");
        userModel2.setFirstName("Jane");
        userModel2.setId(1);
        userModel2.setName("Name");
        userModel2.setSurname("Doe");
        userModel2.setTempPassword("iloveyou");
        userModel2.setType("Type");
        Optional<UserModel> ofResult = Optional.of(userModel2);

        UserModel userModel3 = new UserModel();
        userModel3.setConfirmed(true);
        userModel3.setEmail("jane.doe@example.org");
        userModel3.setFirstName("Jane");
        userModel3.setId(1);
        userModel3.setName("Name");
        userModel3.setSurname("Doe");
        userModel3.setTempPassword("iloveyou");
        userModel3.setType("Type");
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.save((UserModel) any())).thenReturn(userModel3);
        when(userRepository.findById((Integer) any())).thenReturn(ofResult);
        when(userRepository.getById((Integer) any())).thenReturn(userModel1);
        when(userRepository.findByName((String) any())).thenReturn(userModel);
        when(userRepository.existsByName((String) any())).thenReturn(false);
        ClientRepository clientRepository = mock(ClientRepository.class);
        EmailService emailService = new EmailService(new JavaMailSenderImpl());
        ParkingSpotRepository parkingSpotRepository = mock(ParkingSpotRepository.class);
        ParkingOwnerRepository parkingOwnerRepository = mock(ParkingOwnerRepository.class);
        AdminController adminController = new AdminController(new UserService(clientRepository, emailService,
                userRepository, parkingSpotRepository, parkingOwnerRepository, new BCryptPasswordEncoder()),
                mock(UserRepository.class), mock(ParkingOwnerRepository.class));

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        assertEquals("redirect:/admin?dataUpdated=true", adminController.adminEditUserDetails(userDTO));
        verify(userRepository).existsByName((String) any());
        verify(userRepository).findById((Integer) any());
        verify(userRepository).getById((Integer) any());
        verify(userRepository).save((UserModel) any());
        assertTrue(adminController.userService.getAllUnconfirmedOwners().isEmpty());
    }

    @Test
    void testAdminEditUserDetails7() {
        UserModel userModel = new UserModel();
        userModel.setConfirmed(true);
        userModel.setEmail("jane.doe@example.org");
        userModel.setFirstName("Jane");
        userModel.setId(1);
        userModel.setName("Name");
        userModel.setSurname("Doe");
        userModel.setTempPassword("iloveyou");
        userModel.setType("Type");
        UserService userService = mock(UserService.class);
        when(userService.updateUser((UserDTO) any(), anyBoolean(), anyInt())).thenReturn(userModel);
        AdminController adminController = new AdminController(userService, mock(UserRepository.class),
                mock(ParkingOwnerRepository.class));

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        assertEquals("redirect:/admin?dataUpdated=true", adminController.adminEditUserDetails(userDTO));
        verify(userService).updateUser((UserDTO) any(), anyBoolean(), anyInt());
    }

    @Test
    void testAdminConfirmOwner2() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.CONTINUE)).when(this.userService).confirmOwner(anyInt());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/admin/confirmOwner/{id}", 1);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.adminController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(100));
    }

    @Test
    void testGetAdminPage() throws Exception {
        when(this.userService.getAllUnconfirmedOwners()).thenThrow(new ResponseStatusException(HttpStatus.CONTINUE));
        when(this.userService.getUserPage(anyInt(), anyInt())).thenReturn(new PageImpl<>(new ArrayList<>()));
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/admin");
        MockHttpServletRequestBuilder paramResult = getResult.param("page", String.valueOf(1));
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("size", String.valueOf(1));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.adminController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(100));
    }

    @Test
    void testGetAdminPage2() throws Exception {
        UserModel userModel = new UserModel();
        userModel.setConfirmed(true);
        userModel.setEmail("jane.doe@example.org");
        userModel.setFirstName("Jane");
        userModel.setId(1);
        userModel.setName("?");
        userModel.setSurname("Doe");
        userModel.setTempPassword("iloveyou");
        userModel.setType("?");

        ArrayList<UserModel> userModelList = new ArrayList<>();
        userModelList.add(userModel);
        when(this.userService.UserToDTO((UserModel) any())).thenThrow(new ResponseStatusException(HttpStatus.CONTINUE));
        when(this.userService.getAllUnconfirmedOwners()).thenReturn(userModelList);
        when(this.userService.getUserPage(anyInt(), anyInt())).thenReturn(new PageImpl<>(new ArrayList<>()));
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/admin");
        MockHttpServletRequestBuilder paramResult = getResult.param("page", String.valueOf(1));
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("size", String.valueOf(1));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.adminController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(100));
    }
}

