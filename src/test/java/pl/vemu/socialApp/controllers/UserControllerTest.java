package pl.vemu.socialApp.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.vemu.socialApp.entities.User;
import pl.vemu.socialApp.entities.UserDTO;
import pl.vemu.socialApp.exceptions.user.UserByIdNotFoundException;
import pl.vemu.socialApp.exceptions.user.UserWithEmailAlreadyExistException;
import pl.vemu.socialApp.managers.UserManager;
import pl.vemu.socialApp.mappers.UserMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerTest {

    @Autowired
    private UserManager manager;

    @Autowired
    private UserController controller;

    @Autowired
    private UserMapper mapper;

    @Autowired
    private BCryptPasswordEncoder encoder;

    private UserDTO user1, user2;

    @BeforeEach
    public void beforeEach() {
        manager.deleteAll();
        user1 = mapper.toUserDTO(manager.save(new User("name", "surname", "email@siema.pl", "$2y$10$VyNjDhI940DFt2GJ/GT3cuceZoTqcJMBU.Au3471MGa2TAf5FL2FS")));
        user2 = mapper.toUserDTO(manager.save(new User("name2", "surname2", "email2@siema.pl", "$2y$10$.ixE3UkfYhVo73ojfZiLVeLYxvD1Kx3zBmcI3gIQTD1ksydd7a7rO")));
    }

    @Test
    void contextLoads() {
        assertNotNull(controller);
    }

    @Test
    void getUsers() {
        Page<UserDTO> request = controller.getUsers(PageRequest.of(0, 2));

        assertEquals(2, request.getSize());
        assertNotEquals(3, request.getSize());
        assertEquals(user1, request.getContent().get(0));
        assertNotEquals(user2, request.getContent().get(0));
        assertEquals(user2, request.getContent().get(1));
        assertNotEquals(user1, request.getContent().get(1));
    }

    @Test
    void getUserById() throws UserByIdNotFoundException {
        UserDTO request = controller.getUserById(user1.getId());

        assertEquals(user1, request);
        assertNotEquals(user2, request);
        UserByIdNotFoundException exception = assertThrows(UserByIdNotFoundException.class, () -> controller.getUserById(-1L));
        assertEquals("User with id -1 not found!", exception.getMessage());
    }

    @Test
    public void addUser() throws UserWithEmailAlreadyExistException {
        UserDTO user = new UserDTO("Created2", "User4", "Email@email.pl", "created3");
        UserDTO userWithDuplicatedMail = new UserDTO("Created3", "User4", "email@siema.pl", "created3");
        ResponseEntity<UserDTO> request = controller.addUser(user);

        assertNotNull(request.getBody());
        UserDTO userFromRequest = request.getBody();

        assertEquals(user.getName(), userFromRequest.getName());
        assertTrue(encoder.matches(user.getPassword(), manager.findById(userFromRequest.getId()).get().getPassword()));
        UserWithEmailAlreadyExistException exception = assertThrows(UserWithEmailAlreadyExistException.class, () -> controller.addUser(userWithDuplicatedMail));
        assertEquals("User with email " + userWithDuplicatedMail.getEmail() + " already exist!", exception.getMessage());
        assertTrue(request.getHeaders().containsKey("Location"));
    }

    @Test
    void putUser() throws UserWithEmailAlreadyExistException, UserByIdNotFoundException {
        UserDTO user = new UserDTO("Put", "Put", "Put@email.pl", "Put");
        UserDTO userWithDuplicatedMail = new UserDTO("Put", "Put", "email2@siema.pl", "Put");
        ResponseEntity<?> responseEntity = controller.putUser(user1.getId(), user);
        UserDTO userFromDB = manager.findById(user1.getId()).get();

        UserByIdNotFoundException idException = assertThrows(UserByIdNotFoundException.class, () -> controller.putUser(-1L, user));
        assertEquals("User with id -1 not found!", idException.getMessage());
        UserWithEmailAlreadyExistException emailException = assertThrows(UserWithEmailAlreadyExistException.class, () -> controller.putUser(user1.getId(), userWithDuplicatedMail));
        assertEquals("User with email " + userWithDuplicatedMail.getEmail() + " already exist!", emailException.getMessage());

        assertEquals(user.getName(), userFromDB.getName());
        assertEquals(user.getSurname(), userFromDB.getSurname());
        assertEquals(user.getEmail(), userFromDB.getEmail());
        assertTrue(encoder.matches(user.getPassword(), userFromDB.getPassword()));
        assertEquals(ResponseEntity.noContent().build(), responseEntity);
    }

    @Test
        //TODO
    void patchUser() {
    }

    @Test
    public void deleteUsers() {
        ResponseEntity<?> responseEntity = controller.deleteUsers();
        assertEquals(0, manager.findAll(null).getSize());
        assertEquals(ResponseEntity.ok().build(), responseEntity);
    }

    @Test
    public void deleteUserById() throws UserByIdNotFoundException {
        ResponseEntity<?> responseEntity = controller.deleteUserById(user1.getId());
        assertEquals(Optional.empty(), manager.findById(user1.getId()));
        assertEquals(ResponseEntity.ok().build(), responseEntity);
        UserByIdNotFoundException exception = assertThrows(UserByIdNotFoundException.class, () -> controller.deleteUserById(-1L));
        assertEquals("User with id -1 not found!", exception.getMessage());
    }
}