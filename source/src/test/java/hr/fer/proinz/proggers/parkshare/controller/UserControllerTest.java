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
import hr.fer.proinz.proggers.parkshare.dto.StatisticsDTO;
import hr.fer.proinz.proggers.parkshare.dto.UserDTO;
import hr.fer.proinz.proggers.parkshare.model.Parking;
import hr.fer.proinz.proggers.parkshare.model.ParkingOwner;
import hr.fer.proinz.proggers.parkshare.model.UserModel;
import hr.fer.proinz.proggers.parkshare.repo.ClientRepository;
import hr.fer.proinz.proggers.parkshare.repo.ParkingOwnerRepository;
import hr.fer.proinz.proggers.parkshare.repo.ParkingRepository;
import hr.fer.proinz.proggers.parkshare.repo.ParkingSpotOccupancyRepository;
import hr.fer.proinz.proggers.parkshare.repo.ParkingSpotRepository;
import hr.fer.proinz.proggers.parkshare.repo.UserRepository;
import hr.fer.proinz.proggers.parkshare.service.EmailService;
import hr.fer.proinz.proggers.parkshare.service.UserService;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.access.intercept.RunAsUserToken;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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

@ContextConfiguration(classes = {UserController.class})
@ExtendWith(SpringExtension.class)
class UserControllerTest {

    @MockBean
    private ParkingOwnerRepository parkingOwnerRepository;

    @MockBean
    private ParkingRepository parkingRepository;

    @Autowired
    private UserController userController;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @Test
    void testShowUserDetails() {
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
        ClientRepository clientRepository = mock(ClientRepository.class);
        EmailService emailService = new EmailService(new JavaMailSenderImpl());
        UserRepository userRepository1 = mock(UserRepository.class);
        ParkingSpotRepository parkingSpotRepository = mock(ParkingSpotRepository.class);
        ParkingOwnerRepository parkingOwnerRepository = mock(ParkingOwnerRepository.class);
        UserController userController = new UserController(
                new UserService(clientRepository, emailService, userRepository1, parkingSpotRepository, parkingOwnerRepository,
                        new BCryptPasswordEncoder()),
                userRepository, mock(ParkingOwnerRepository.class), mock(ClientRepository.class), mock(ParkingRepository.class),
                mock(ParkingSpotRepository.class), mock(ParkingSpotOccupancyRepository.class));
        ModelMap modelMap = new ModelMap();
        assertEquals("userpage",
                userController.showUserDetails(modelMap, new TestingAuthenticationToken("Principal", "Credentials")));
        verify(userRepository).findByEmail((String) any());
        Object getResult = modelMap.get("user");
        assertEquals("UserDTO{id=1, username='Name', firstName='Jane', lastName='Doe', mail='jane.doe@example.org',"
                + " password='iloveyou', confirmationPassword='null', role='Type', iban='null', walletBalance=null,"
                + " confirmed=true}", getResult.toString());
        assertFalse(((UserDTO) getResult).isOwner());
        assertNull(((UserDTO) getResult).getWalletBalance());
        assertEquals("Name", ((UserDTO) getResult).getUsername());
        assertEquals("Jane", ((UserDTO) getResult).getUserfirstname());
        assertEquals("iloveyou", ((UserDTO) getResult).getPassword());
        assertEquals("jane.doe@example.org", ((UserDTO) getResult).getMail());
        assertEquals("Doe", ((UserDTO) getResult).getLastName());
        assertEquals(1, ((UserDTO) getResult).getId().intValue());
        assertNull(((UserDTO) getResult).getIban());
        assertTrue(userController.userService.getAllUnconfirmedOwners().isEmpty());
    }

    @Test
    void testShowUserDetails2() {
        UserDTO userDTO = mock(UserDTO.class);
        when(userDTO.getId()).thenReturn(1);
        when(userDTO.isOwner()).thenReturn(true);
        UserService userService = mock(UserService.class);
        when(userService.UserToDTO((UserModel) any())).thenReturn(userDTO);

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

        Parking parking = new Parking();
        parking.setDescription("The characteristics of someone or something");
        parking.setEntrancepointx(BigDecimal.valueOf(42L));
        parking.setEntrancepointy(BigDecimal.valueOf(42L));
        parking.setHourlyPrice(BigDecimal.valueOf(42L));
        parking.setId(1);
        parking.setParkingName("Parking Name");
        parking.setParkingPhoto("alice.liddell@example.org");
        ParkingRepository parkingRepository = mock(ParkingRepository.class);
        when(parkingRepository.findById((Integer) any())).thenReturn(Optional.of(parking));
        ParkingSpotOccupancyRepository parkingSpotOccupancyRepository = mock(ParkingSpotOccupancyRepository.class);
        when(parkingSpotOccupancyRepository.getAllById_Userid((Integer) any())).thenReturn(new ArrayList<>());
        UserController userController = new UserController(userService, userRepository, mock(ParkingOwnerRepository.class),
                mock(ClientRepository.class), parkingRepository, mock(ParkingSpotRepository.class),
                parkingSpotOccupancyRepository);
        ModelMap modelMap = new ModelMap();
        assertEquals("userpage",
                userController.showUserDetails(modelMap, new TestingAuthenticationToken("Principal", "Credentials")));
        verify(userService).UserToDTO((UserModel) any());
        verify(userDTO, atLeast(1)).getId();
        verify(userDTO).isOwner();
        verify(userRepository).findByEmail((String) any());
        verify(parkingRepository).findById((Integer) any());
        verify(parkingSpotOccupancyRepository).getAllById_Userid((Integer) any());
        Object getResult = modelMap.get("statistics");
        assertEquals(7, ((Collection<StatisticsDTO>) getResult).size());
        StatisticsDTO getResult1 = ((List<StatisticsDTO>) getResult).get(2);
        assertEquals(Double.NaN, getResult1.getY());
        StatisticsDTO getResult2 = ((List<StatisticsDTO>) getResult).get(0);
        assertEquals(Double.NaN, getResult2.getY());
        assertEquals("Monday", getResult2.getX());
        assertEquals("Wednesday", getResult1.getX());
        StatisticsDTO getResult3 = ((List<StatisticsDTO>) getResult).get(1);
        assertEquals("Tuesday", getResult3.getX());
        StatisticsDTO getResult4 = ((List<StatisticsDTO>) getResult).get(6);
        assertEquals("Sunday", getResult4.getX());
        StatisticsDTO getResult5 = ((List<StatisticsDTO>) getResult).get(4);
        assertEquals("Friday", getResult5.getX());
        StatisticsDTO getResult6 = ((List<StatisticsDTO>) getResult).get(5);
        assertEquals("Saturday", getResult6.getX());
        assertEquals(Double.NaN, getResult5.getY());
        assertEquals(Double.NaN, getResult6.getY());
        assertEquals(Double.NaN, getResult3.getY());
        assertEquals(Double.NaN, getResult4.getY());
    }

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
    void testEditUserDetails() {
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
        UserController userController = new UserController(userService, userRepository1, mock(ParkingOwnerRepository.class),
                mock(ClientRepository.class), mock(ParkingRepository.class), mock(ParkingSpotRepository.class),
                mock(ParkingSpotOccupancyRepository.class));
        UserDTO updatedUser = new UserDTO();
        ModelMap model = new ModelMap();
        ModelAndView actualEditUserDetailsResult = userController.editUserDetails(updatedUser, model,
                new TestingAuthenticationToken("Principal", "Credentials"));
        assertTrue(actualEditUserDetailsResult.isReference());
        Map<String, Object> model1 = actualEditUserDetailsResult.getModel();
        assertSame(model1, actualEditUserDetailsResult.getModelMap());
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
    void testEditUserDetails2() {
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
        UserController userController = new UserController(userService, userRepository1, mock(ParkingOwnerRepository.class),
                mock(ClientRepository.class), mock(ParkingRepository.class), mock(ParkingSpotRepository.class),
                mock(ParkingSpotOccupancyRepository.class));
        UserDTO updatedUser = new UserDTO();
        ModelMap model = new ModelMap();
        ModelAndView actualEditUserDetailsResult = userController.editUserDetails(updatedUser, model,
                new TestingAuthenticationToken("Principal", "Credentials"));
        assertTrue(actualEditUserDetailsResult.isReference());
        Map<String, Object> model1 = actualEditUserDetailsResult.getModel();
        assertSame(model1, actualEditUserDetailsResult.getModelMap());
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
    void testEditUserDetails3() {
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
        UserController userController = new UserController(userService, userRepository1, mock(ParkingOwnerRepository.class),
                mock(ClientRepository.class), mock(ParkingRepository.class), mock(ParkingSpotRepository.class),
                mock(ParkingSpotOccupancyRepository.class));
        UserDTO updatedUser = new UserDTO();
        ModelMap model = new ModelMap();
        ModelAndView actualEditUserDetailsResult = userController.editUserDetails(updatedUser, model,
                new TestingAuthenticationToken("Principal", "Credentials"));
        assertTrue(actualEditUserDetailsResult.isReference());
        Map<String, Object> model1 = actualEditUserDetailsResult.getModel();
        assertSame(model1, actualEditUserDetailsResult.getModelMap());
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
    void testEditUserDetails4() {
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
        when(userService.UserToDTO((UserModel) any())).thenThrow(new ResponseStatusException(HttpStatus.CONTINUE));
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
        UserController userController = new UserController(userService, userRepository, mock(ParkingOwnerRepository.class),
                mock(ClientRepository.class), mock(ParkingRepository.class), mock(ParkingSpotRepository.class),
                mock(ParkingSpotOccupancyRepository.class));
        UserDTO updatedUser = new UserDTO();
        ModelMap model = new ModelMap();
        assertThrows(ResponseStatusException.class, () -> userController.editUserDetails(updatedUser, model,
                new TestingAuthenticationToken("Principal", "Credentials")));
        verify(userService).UserToDTO((UserModel) any());
        verify(userService).updateUser((UserDTO) any(), anyBoolean(), anyInt());
        verify(userRepository, atLeast(1)).findByEmail((String) any());
    }

    @Test
    void testEditUserDetails5() {
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
        when(userService.UserToDTO((UserModel) any())).thenReturn(new UserDTO(1, "janedoe", "Jane", "Doe", "loggedIn",
                "iloveyou", "loggedIn", "loggedIn", BigDecimal.valueOf(42L), true));
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
        UserController userController = new UserController(userService, userRepository, mock(ParkingOwnerRepository.class),
                mock(ClientRepository.class), mock(ParkingRepository.class), mock(ParkingSpotRepository.class),
                mock(ParkingSpotOccupancyRepository.class));
        UserDTO userDTO = new UserDTO();
        ModelMap model = new ModelMap();
        ModelAndView actualEditUserDetailsResult = userController.editUserDetails(userDTO, model,
                new TestingAuthenticationToken("Principal", "Credentials"));
        assertTrue(actualEditUserDetailsResult.isReference());
        Map<String, Object> model1 = actualEditUserDetailsResult.getModel();
        assertSame(model1, actualEditUserDetailsResult.getModelMap());
        Object getResult = model1.get("information");
        assertEquals(1, ((Collection<MessageDTO>) getResult).size());
        MessageDTO getResult1 = ((List<MessageDTO>) getResult).get(0);
        assertEquals("Success!", getResult1.getMsg());
        assertEquals("42", ((UserDTO) model1.get("user")).getWalletBalance().toString());
        assertEquals("Your data has been updated.", getResult1.getDesc());
        verify(userService, atLeast(1)).UserToDTO((UserModel) any());
        verify(userService).updateUser((UserDTO) any(), anyBoolean(), anyInt());
        verify(userRepository, atLeast(1)).findByEmail((String) any());
        assertFalse(userDTO.isOwner());
    }

    @Test
    void testEditUserDetails6() {
        UserModel userModel = new UserModel();
        userModel.setConfirmed(true);
        userModel.setEmail("jane.doe@example.org");
        userModel.setFirstName("Jane");
        userModel.setId(1);
        userModel.setName("Name");
        userModel.setSurname("Doe");
        userModel.setTempPassword("iloveyou");
        userModel.setType("Type");
        UserDTO userDTO = mock(UserDTO.class);
        when(userDTO.getId()).thenReturn(1);
        when(userDTO.isOwner()).thenReturn(true);
        UserService userService = mock(UserService.class);
        when(userService.UserToDTO((UserModel) any())).thenReturn(userDTO);
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

        Parking parking = new Parking();
        parking.setDescription("The characteristics of someone or something");
        parking.setEntrancepointx(BigDecimal.valueOf(42L));
        parking.setEntrancepointy(BigDecimal.valueOf(42L));
        parking.setHourlyPrice(BigDecimal.valueOf(42L));
        parking.setId(1);
        parking.setParkingName("Parking Name");
        parking.setParkingPhoto("alice.liddell@example.org");
        ParkingRepository parkingRepository = mock(ParkingRepository.class);
        when(parkingRepository.findById((Integer) any())).thenReturn(Optional.of(parking));
        UserController userController = new UserController(userService, userRepository, mock(ParkingOwnerRepository.class),
                mock(ClientRepository.class), parkingRepository, mock(ParkingSpotRepository.class),
                mock(ParkingSpotOccupancyRepository.class));
        UserDTO userDTO1 = new UserDTO();
        ModelMap model = new ModelMap();
        ModelAndView actualEditUserDetailsResult = userController.editUserDetails(userDTO1, model,
                new TestingAuthenticationToken("Principal", "Credentials"));
        assertTrue(actualEditUserDetailsResult.isReference());
        Map<String, Object> model1 = actualEditUserDetailsResult.getModel();
        assertSame(model1, actualEditUserDetailsResult.getModelMap());
        Object getResult = model1.get("information");
        assertEquals(1, ((Collection<MessageDTO>) getResult).size());
        MessageDTO getResult1 = ((List<MessageDTO>) getResult).get(0);
        assertEquals("Your data has been updated.", getResult1.getDesc());
        assertEquals("Success!", getResult1.getMsg());
        verify(userService, atLeast(1)).UserToDTO((UserModel) any());
        verify(userService).updateUser((UserDTO) any(), anyBoolean(), anyInt());
        verify(userDTO).getId();
        verify(userDTO).isOwner();
        verify(userRepository, atLeast(1)).findByEmail((String) any());
        verify(parkingRepository).findById((Integer) any());
        assertFalse(userDTO1.isOwner());
    }

    @Test
    void testEditUserDetails7() {
        UserModel userModel = new UserModel();
        userModel.setConfirmed(true);
        userModel.setEmail("jane.doe@example.org");
        userModel.setFirstName("Jane");
        userModel.setId(1);
        userModel.setName("Name");
        userModel.setSurname("Doe");
        userModel.setTempPassword("iloveyou");
        userModel.setType("Type");
        UserDTO userDTO = mock(UserDTO.class);
        when(userDTO.getId()).thenReturn(1);
        when(userDTO.isOwner()).thenReturn(true);
        UserService userService = mock(UserService.class);
        when(userService.UserToDTO((UserModel) any())).thenReturn(userDTO);
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
        ParkingRepository parkingRepository = mock(ParkingRepository.class);
        when(parkingRepository.findById((Integer) any())).thenThrow(new ResponseStatusException(HttpStatus.CONTINUE));
        UserController userController = new UserController(userService, userRepository, mock(ParkingOwnerRepository.class),
                mock(ClientRepository.class), parkingRepository, mock(ParkingSpotRepository.class),
                mock(ParkingSpotOccupancyRepository.class));
        UserDTO updatedUser = new UserDTO();
        ModelMap model = new ModelMap();
        assertThrows(ResponseStatusException.class, () -> userController.editUserDetails(updatedUser, model,
                new TestingAuthenticationToken("Principal", "Credentials")));
        verify(userService).UserToDTO((UserModel) any());
        verify(userService).updateUser((UserDTO) any(), anyBoolean(), anyInt());
        verify(userDTO).getId();
        verify(userDTO).isOwner();
        verify(userRepository, atLeast(1)).findByEmail((String) any());
        verify(parkingRepository).findById((Integer) any());
    }

    @Test
    void testEditUserDetails8() {
        UserModel userModel = new UserModel();
        userModel.setConfirmed(true);
        userModel.setEmail("jane.doe@example.org");
        userModel.setFirstName("Jane");
        userModel.setId(1);
        userModel.setName("Name");
        userModel.setSurname("Doe");
        userModel.setTempPassword("iloveyou");
        userModel.setType("Type");
        UserDTO userDTO = mock(UserDTO.class);
        when(userDTO.getId()).thenThrow(new ResponseStatusException(HttpStatus.CONTINUE));
        when(userDTO.isOwner()).thenReturn(true);
        UserService userService = mock(UserService.class);
        when(userService.UserToDTO((UserModel) any())).thenReturn(userDTO);
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

        Parking parking = new Parking();
        parking.setDescription("The characteristics of someone or something");
        parking.setEntrancepointx(BigDecimal.valueOf(42L));
        parking.setEntrancepointy(BigDecimal.valueOf(42L));
        parking.setHourlyPrice(BigDecimal.valueOf(42L));
        parking.setId(1);
        parking.setParkingName("Parking Name");
        parking.setParkingPhoto("alice.liddell@example.org");
        ParkingRepository parkingRepository = mock(ParkingRepository.class);
        when(parkingRepository.findById((Integer) any())).thenReturn(Optional.of(parking));
        UserController userController = new UserController(userService, userRepository, mock(ParkingOwnerRepository.class),
                mock(ClientRepository.class), parkingRepository, mock(ParkingSpotRepository.class),
                mock(ParkingSpotOccupancyRepository.class));
        UserDTO updatedUser = new UserDTO();
        ModelMap model = new ModelMap();
        assertThrows(ResponseStatusException.class, () -> userController.editUserDetails(updatedUser, model,
                new TestingAuthenticationToken("Principal", "Credentials")));
        verify(userService).UserToDTO((UserModel) any());
        verify(userService).updateUser((UserDTO) any(), anyBoolean(), anyInt());
        verify(userDTO).getId();
        verify(userDTO).isOwner();
        verify(userRepository, atLeast(1)).findByEmail((String) any());
    }


    @Test
    void testCreateParking() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/profile/createParking");
        MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("createParkingDTO"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/loginRouter"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/loginRouter"));
    }

    @Test
    void testCreateParking2() throws Exception {
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/profile/createParking");
        postResult.characterEncoding("Encoding");
        MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(postResult)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("createParkingDTO"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/loginRouter"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/loginRouter"));
    }

    @Test
    void testCreateParking3() throws Exception {
        UserModel userModel = new UserModel();
        userModel.setConfirmed(true);
        userModel.setEmail("jane.doe@example.org");
        userModel.setFirstName("Jane");
        userModel.setId(1);
        userModel.setName("Name");
        userModel.setSurname("Doe");
        userModel.setTempPassword("iloveyou");
        userModel.setType("Type");
        when(this.userRepository.findByEmail((String) any())).thenReturn(userModel);
        when(this.parkingRepository.existsById((Integer) any())).thenReturn(true);
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/profile/createParking");
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        postResult.principal(new RunAsUserToken("Key", "Principal", "Credentials", authorities, Authentication.class));
        MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(postResult)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.model().attributeExists("createParkingDTO", "errors"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/profile"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/profile"));
    }

    @Test
    void testCreateParkingForm() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/profile/createParking");
        MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/loginRouter"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/loginRouter"));
    }

    @Test
    void testCreateParkingForm2() throws Exception {
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/profile/createParking");
        getResult.characterEncoding("Encoding");
        MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/loginRouter"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/loginRouter"));
    }

    @Test
    void testCreateParkingForm3() throws Exception {
        UserModel userModel = new UserModel();
        userModel.setConfirmed(true);
        userModel.setEmail("jane.doe@example.org");
        userModel.setFirstName("Jane");
        userModel.setId(1);
        userModel.setName("Name");
        userModel.setSurname("Doe");
        userModel.setTempPassword("iloveyou");
        userModel.setType("Type");
        when(this.userRepository.findByEmail((String) any())).thenReturn(userModel);
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/profile/createParking");
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        getResult.principal(new RunAsUserToken("Key", "Principal", "Credentials", authorities, Authentication.class));
        MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/profile"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/profile"));
    }

    @Test
    void testCreateParkingSpot() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/profile/createParkingSpot")
                .param("continueOrFinish", "foo");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    void testDeleteParkingSpot() throws Exception {
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/profile/deleteParkingSpot");
        MockHttpServletRequestBuilder requestBuilder = postResult.param("parkingSpotNumber", String.valueOf(1));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    void testEditParking() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/profile/editParking");
        MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("createParkingDTO"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/loginRouter"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/loginRouter"));
    }

    @Test
    void testEditParking2() throws Exception {
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/profile/editParking");
        postResult.characterEncoding("Encoding");
        MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(postResult)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("createParkingDTO"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/loginRouter"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/loginRouter"));
    }

    @Test
    void testEditParkingForm() throws Exception {
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/profile/editParking");
        MockHttpServletRequestBuilder paramResult = getResult.param("page", String.valueOf(1));
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("size", String.valueOf(1));
        MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/loginRouter"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/loginRouter"));
    }

    @Test
    void testEditParkingSpot() throws Exception {
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/profile/editParkingSpot");
        MockHttpServletRequestBuilder requestBuilder = postResult.param("parkingSpotId", String.valueOf(1));
        MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("parkingSpotDTO"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/loginRouter"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/loginRouter"));
    }

    @Test
    void testEditParkingSpot2() throws Exception {
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/profile/editParkingSpot");
        MockHttpServletRequestBuilder requestBuilder = getResult.param("parkingSpotId", String.valueOf(1));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    void testRegister() throws Exception {
        UserModel userModel = new UserModel();
        userModel.setConfirmed(true);
        userModel.setEmail("jane.doe@example.org");
        userModel.setFirstName("Jane");
        userModel.setId(1);
        userModel.setName("Name");
        userModel.setSurname("Doe");
        userModel.setTempPassword("iloveyou");
        userModel.setType("Type");
        when(this.userService.registerNewUser((hr.fer.proinz.proggers.parkshare.dto.RegisterFormDTO) any()))
                .thenReturn(userModel);

        ParkingOwner parkingOwner = new ParkingOwner();
        parkingOwner.setIban("Iban");
        parkingOwner.setId(1);
        parkingOwner.setIdPicture("Id Picture");
        Optional<ParkingOwner> ofResult = Optional.of(parkingOwner);

        ParkingOwner parkingOwner1 = new ParkingOwner();
        parkingOwner1.setIban("Iban");
        parkingOwner1.setId(1);
        parkingOwner1.setIdPicture("Id Picture");
        when(this.parkingOwnerRepository.save((ParkingOwner) any())).thenReturn(parkingOwner1);
        when(this.parkingOwnerRepository.findById((Integer) any())).thenReturn(ofResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/");
        MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(4))
                .andExpect(
                        MockMvcResultMatchers.model().attributeExists("information", "loggedIn", "registerForm", "registerFormDTO"))
                .andExpect(MockMvcResultMatchers.view().name("index"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("index"));
    }

    @Test
    void testRegister2() throws Exception {
        UserModel userModel = new UserModel();
        userModel.setConfirmed(true);
        userModel.setEmail("jane.doe@example.org");
        userModel.setFirstName("Jane");
        userModel.setId(1);
        userModel.setName("Name");
        userModel.setSurname("Doe");
        userModel.setTempPassword("iloveyou");
        userModel.setType("Type");
        when(this.userService.registerNewUser((hr.fer.proinz.proggers.parkshare.dto.RegisterFormDTO) any()))
                .thenReturn(userModel);

        ParkingOwner parkingOwner = new ParkingOwner();
        parkingOwner.setIban("Iban");
        parkingOwner.setId(1);
        parkingOwner.setIdPicture("Id Picture");
        Optional<ParkingOwner> ofResult = Optional.of(parkingOwner);
        when(this.parkingOwnerRepository.save((ParkingOwner) any()))
                .thenThrow(new ResponseStatusException(HttpStatus.CONTINUE));
        when(this.parkingOwnerRepository.findById((Integer) any())).thenReturn(ofResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/");
        MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(4))
                .andExpect(
                        MockMvcResultMatchers.model().attributeExists("errors", "loggedIn", "registerForm", "registerFormDTO"))
                .andExpect(MockMvcResultMatchers.view().name("index"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("index"));
    }

    @Test
    void testRegister3() throws Exception {
        UserModel userModel = new UserModel();
        userModel.setConfirmed(true);
        userModel.setEmail("jane.doe@example.org");
        userModel.setFirstName("Jane");
        userModel.setId(1);
        userModel.setName("Name");
        userModel.setSurname("Doe");
        userModel.setTempPassword("iloveyou");
        userModel.setType("client");
        doNothing().when(this.userService).sendMail((UserModel) any(), (String) any());
        when(this.userService.registerNewUser((hr.fer.proinz.proggers.parkshare.dto.RegisterFormDTO) any()))
                .thenReturn(userModel);

        ParkingOwner parkingOwner = new ParkingOwner();
        parkingOwner.setIban("Iban");
        parkingOwner.setId(1);
        parkingOwner.setIdPicture("Id Picture");
        Optional<ParkingOwner> ofResult = Optional.of(parkingOwner);
        when(this.parkingOwnerRepository.save((ParkingOwner) any())).thenThrow(new InvalidParameterException("client"));
        when(this.parkingOwnerRepository.findById((Integer) any())).thenReturn(ofResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/");
        MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(4))
                .andExpect(
                        MockMvcResultMatchers.model().attributeExists("information", "loggedIn", "registerForm", "registerFormDTO"))
                .andExpect(MockMvcResultMatchers.view().name("index"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("index"));
    }

    @Test
    void testRegister4() throws Exception {
        UserModel userModel = new UserModel();
        userModel.setConfirmed(true);
        userModel.setEmail("jane.doe@example.org");
        userModel.setFirstName("Jane");
        userModel.setId(1);
        userModel.setName("Name");
        userModel.setSurname("Doe");
        userModel.setTempPassword("iloveyou");
        userModel.setType("client");
        doThrow(new ResponseStatusException(HttpStatus.CONTINUE)).when(this.userService)
                .sendMail((UserModel) any(), (String) any());
        when(this.userService.registerNewUser((hr.fer.proinz.proggers.parkshare.dto.RegisterFormDTO) any()))
                .thenReturn(userModel);

        ParkingOwner parkingOwner = new ParkingOwner();
        parkingOwner.setIban("Iban");
        parkingOwner.setId(1);
        parkingOwner.setIdPicture("Id Picture");
        Optional<ParkingOwner> ofResult = Optional.of(parkingOwner);
        when(this.parkingOwnerRepository.save((ParkingOwner) any())).thenThrow(new InvalidParameterException("client"));
        when(this.parkingOwnerRepository.findById((Integer) any())).thenReturn(ofResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/");
        MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(4))
                .andExpect(
                        MockMvcResultMatchers.model().attributeExists("errors", "loggedIn", "registerForm", "registerFormDTO"))
                .andExpect(MockMvcResultMatchers.view().name("index"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("index"));
    }

    @Test
    void testShowCreateParkingSpot() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/profile/createParkingSpot");
        MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/loginRouter"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/loginRouter"));
    }

    @Test
    void testShowCreateParkingSpot2() throws Exception {
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/profile/createParkingSpot");
        getResult.characterEncoding("Encoding");
        MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/loginRouter"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/loginRouter"));
    }

    @Test
    void testShowCreateParkingSpot3() throws Exception {
        UserModel userModel = new UserModel();
        userModel.setConfirmed(true);
        userModel.setEmail("jane.doe@example.org");
        userModel.setFirstName("Jane");
        userModel.setId(1);
        userModel.setName("Name");
        userModel.setSurname("Doe");
        userModel.setTempPassword("iloveyou");
        userModel.setType("Type");
        when(this.userRepository.findByEmail((String) any())).thenReturn(userModel);
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/profile/createParkingSpot");
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        getResult.principal(new RunAsUserToken("Key", "Principal", "Credentials", authorities, Authentication.class));
        MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.model().attributeExists("loggedIn", "spot"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/profile"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/profile?loggedIn=true"));
    }

    @Test
    void testShowRegistrationForm() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/");
        MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.model().attributeExists("loggedIn", "registerForm"))
                .andExpect(MockMvcResultMatchers.view().name("index"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("index"));
    }

    @Test
    void testShowRegistrationForm2() throws Exception {
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/");
        getResult.characterEncoding("Encoding");
        MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.model().attributeExists("loggedIn", "registerForm"))
                .andExpect(MockMvcResultMatchers.view().name("index"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("index"));
    }

    @Test
    void testShowRegistrationForm3() throws Exception {
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/");
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        getResult.principal(new RunAsUserToken("Key", "Principal", "Credentials", authorities, Authentication.class));
        MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/loginRouter"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/loginRouter"));
    }

    @Test
    void testVerifyUser() throws Exception {
        when(this.userService.verify((String) any())).thenReturn(true);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/confirm").param("code", "foo");
        MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/?verifySuccess=true"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/?verifySuccess=true"));
    }

    @Test
    void testVerifyUser2() throws Exception {
        when(this.userService.verify((String) any())).thenThrow(new ResponseStatusException(HttpStatus.CONTINUE));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/confirm").param("code", "foo");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(100));
    }

    @Test
    void testVerifyUser3() throws Exception {
        when(this.userService.verify((String) any())).thenReturn(false);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/confirm").param("code", "foo");
        MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/?verifyFailure=true"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/?verifyFailure=true"));
    }
}

