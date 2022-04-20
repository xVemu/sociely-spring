/*
package pl.vemu.sociely.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.vemu.sociely.dtos.UserDTO;
import pl.vemu.sociely.entities.User;
import pl.vemu.sociely.exceptions.user.UserByIdNotFoundException;
import pl.vemu.sociely.exceptions.user.UserWithEmailAlreadyExistException;
import pl.vemu.sociely.services.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerTest {

    //    TODO mock
    @Autowired
    private UserService manager;

    @Autowired
    private UserController controller;

    @Autowired
    private BCryptPasswordEncoder encoder;

    private UserDTO user1, user2;

    @BeforeEach
    public void beforeEach() {
        manager.deleteAll();
        user1 = manager.save(new User("name", "surname", "email@siema.pl", "$2y$10$VyNjDhI940DFt2GJ/GT3cuceZoTqcJMBU.Au3471MGa2TAf5FL2FS"));
        user2 = manager.save(new User("name2", "surname2", "email2@siema.pl", "$2y$10$.ixE3UkfYhVo73ojfZiLVeLYxvD1Kx3zBmcI3gIQTD1ksydd7a7rO"));
    }

    @Test
    void contextLoads() {
        assertNotNull(controller);
    }

    @Test
    void getUsers() {
        Page<UserDTO> request = controller.getUsers(PageRequest.of(0, 2));

        assertEquals(2, request.getSize());
        assertEquals(user1, request.getContent().get(0));
        assertNotEquals(user2, request.getContent().get(0));
        assertEquals(user2, request.getContent().get(1));
        assertNotEquals(user1, request.getContent().get(1));
    }

    @Test
    void getUserById() throws UserByIdNotFoundException {
        UserDTO request = controller.getUserById(user1.id());

        assertEquals(user1, request);
        assertNotEquals(user2, request);
        UserByIdNotFoundException exception = assertThrows(UserByIdNotFoundException.class, () -> controller.getUserById(-1L));
        assertEquals("User with id -1 not found!", exception.getMessage());
    }

    @Test
    public void addUser() throws UserWithEmailAlreadyExistException {
        UserDTO user = new UserDTO("Created2", "User4", "Email@email.pl", "created3", "admin");
        UserDTO userWithDuplicatedMail = new UserDTO("Created3", "User4", "email@siema.pl", "created3", "admin");
        ResponseEntity<UserDTO> request = controller.addUser(user);

        assertNotNull(request.getBody());
        UserDTO userFromRequest = request.getBody();

        assertEquals(user.name(), userFromRequest.name());
        assertTrue(encoder.matches(user.password(), manager.findById(userFromRequest.id()).get().password()));
        UserWithEmailAlreadyExistException exception = assertThrows(UserWithEmailAlreadyExistException.class, () -> controller.addUser(userWithDuplicatedMail));
        assertEquals("User with email " + userWithDuplicatedMail.email() + " already exist!", exception.getMessage());
        assertTrue(request.getHeaders().containsKey("Location"));
    }

    @Test
    void putUser() throws UserWithEmailAlreadyExistException, UserByIdNotFoundException {
        UserDTO user = new UserDTO("Put", "Put", "Put@email.pl", "Put", "admin");
        UserDTO userWithDuplicatedMail = new UserDTO("Put", "Put", "email2@siema.pl", "Put", "admin");
        ResponseEntity<?> responseEntity = controller.putUser(user1.id(), user);
        UserDTO userFromDB = manager.findById(user1.id()).get();

        UserByIdNotFoundException idException = assertThrows(UserByIdNotFoundException.class, () -> controller.putUser(-1L, user));
        assertEquals("User with id -1 not found!", idException.getMessage());
        UserWithEmailAlreadyExistException emailException = assertThrows(UserWithEmailAlreadyExistException.class, () -> controller.putUser(user1.id(), userWithDuplicatedMail));
        assertEquals("User with email " + userWithDuplicatedMail.email() + " already exist!", emailException.getMessage());

        assertEquals(user.name(), userFromDB.name());
        assertEquals(user.surname(), userFromDB.surname());
        assertEquals(user.email(), userFromDB.email());
        assertTrue(encoder.matches(user.password(), userFromDB.password()));
        assertEquals(ResponseEntity.noContent().build(), responseEntity);
    }

    @Test
        //TODO
    void patchUser() {
    }

    @Test
    public void deleteUsers() {
        controller.deleteUsers();
        assertEquals(0, manager.getAll(null).getSize());
//        assertEquals(ResponseEntity.ok().build(), responseEntity);
    }

    @Test
    public void deleteUserById() throws UserByIdNotFoundException {
        controller.deleteUserById(user1.id());
        assertEquals(Optional.empty(), manager.findById(user1.id()));
//        assertEquals(ResponseEntity.ok().build(), responseEntity);
        UserByIdNotFoundException exception = assertThrows(UserByIdNotFoundException.class, () -> controller.deleteUserById(-1L));
        assertEquals("User with id -1 not found!", exception.getMessage());
    }
}*/
