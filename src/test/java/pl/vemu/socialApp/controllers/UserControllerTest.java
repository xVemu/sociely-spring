package pl.vemu.socialApp.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.vemu.socialApp.Main;
import pl.vemu.socialApp.entities.User;
import pl.vemu.socialApp.exceptions.user.UserByIdNotFoundException;
import pl.vemu.socialApp.exceptions.user.UserWithEmailAlreadyExistException;
import pl.vemu.socialApp.managers.UserManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Main.class)
class UserControllerTest { //TODO commit, then tests

    @Autowired
    private UserManager manager;

    @Autowired
    private UserController controller;

    private User user1, user2;

    @BeforeEach
    public void beforeEach() {
        user1 = manager.save(new User("name", "surname", "email", "$2y$10$VyNjDhI940DFt2GJ/GT3cuceZoTqcJMBU.Au3471MGa2TAf5FL2FS"));
        user2 = manager.save(new User("name2", "surname2", "email2", "$2y$10$.ixE3UkfYhVo73ojfZiLVeLYxvD1Kx3zBmcI3gIQTD1ksydd7a7rO"));
    }

    @AfterEach
    public void afterEach() {
        manager.deleteAll();
    }

    @Test
    void shouldReturnTwoUsers() {
        assertEquals(2, controller.getUsers(PageRequest.of(0, 2)).getSize());
    }

    @Test
    public void shouldNotGetThreeUsers() {
        assertNotEquals(3, controller.getUsers(PageRequest.of(0, 2)).getSize());
    }

    @Test
    public void shouldGetUserById() throws UserByIdNotFoundException {
        assertEquals(user1, controller.getUserById(user1.getId()));
    }

    @Test
    public void shouldNotGetUserById() throws UserByIdNotFoundException {
        assertNotEquals(user2, controller.getUserById(user1.getId()));
    }

    @Test
    public void shouldThrowErrorGetUserById() {
        UserByIdNotFoundException exception = assertThrows(UserByIdNotFoundException.class, () -> controller.getUserById(-1L));
        assertEquals("User with id -1 not found!", exception.getMessage());
    }

    @Test
    public void addUser() throws UserWithEmailAlreadyExistException {
        User user = new User("Created", "User", "Email", "created3");
        ResponseEntity<User> createdUser = controller.addUser(user);
        assertEquals(createdUser.getBody().getPassword(), "$2y$10$VGoh.1.bImhrTyo3iTPa.uXFzIPuOgr0uL.ts4dL7V8AkNexc0W5K");
    }

    @Test
    public void shouldThrowErrorAddUser() {
        UserWithEmailAlreadyExistException exception = assertThrows(UserWithEmailAlreadyExistException.class,
                () -> controller.addUser(new User("Created", "User", "email2", "created3")));
        assertEquals("User with email email2 already exist!", exception.getMessage());
    }

    @Test
    void putUser() {
    }

    @Test
    void patchUser() {
    }

    @Test
    public void deleteUsers() {
        controller.deleteUsers();
    }

    @Test
    public void shouldDeleteUserById() throws UserByIdNotFoundException {
        ResponseEntity<?> responseEntity = controller.deleteUserById(user1.getId());
        assertEquals(Optional.empty(), manager.findById(user1.getId()));
        assertEquals(ResponseEntity.ok().build(), responseEntity);
    }

    @Test
    public void shouldThrowErrorDeleteUserById() {
        UserByIdNotFoundException exception = assertThrows(UserByIdNotFoundException.class, () -> controller.deleteUserById(-1L));
        assertEquals("User with id -1 not found!", exception.getMessage());
    }
}