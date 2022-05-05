package pl.vemu.sociely.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.vemu.sociely.dtos.request.UserDtoRequest;
import pl.vemu.sociely.entities.User;
import pl.vemu.sociely.exceptions.user.UserByIdNotFound;
import pl.vemu.sociely.exceptions.user.UserWithEmailAlreadyExist;
import pl.vemu.sociely.mappers.UserMapper;
import pl.vemu.sociely.mappers.UserMapperImpl;
import pl.vemu.sociely.repositories.UserRepository;
import pl.vemu.sociely.utils.Roles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private final List<User> users = new ArrayList<>(Arrays.asList(
            new User(0L, "Jesse", "Pinkman", "elisabeth.goldner@yahoo.com",
                     "$2a$12$oCXMCYmgaI/cVtUnUvcR3uSqAf6P8O9GQ.5rFjBuQb1Cl4wu5X1eC" /*password: uz2iov8599*/,
                     Roles.USER, Collections.emptyList()
            ), new User(1L, "Todd", "Alquist", "randall.muller@yahoo.com",
                        "$2a$12$i8XVImk2GVR25s/JAEhw7OWx5Y8uE9G5AjHcNZSFjkUewbZVETevS" /*password: zt1uk678ydtck7*/,
                        Roles.ADMIN, Collections.emptyList()
            ), new User(2L, "Adam", null, "joan.macejkovic@hotmail.com",
                        "$2a$12$eX7ZhRC9Tw7GJTXLxwNcFutWbqSe22No4y6j1qFJoUPSReO9UkUka" /*password: 519kujoju*/,
                        Roles.MODERATOR, Collections.emptyList()
            )));
    @Mock
    UserRepository repository;
    @Spy
    BCryptPasswordEncoder encoder;
    UserMapper mapper = new UserMapperImpl();
    UserService service;

    @BeforeEach
    void setUp() {
        mapper.setEncoder(encoder);
        service = new UserService(repository, mapper);
    }

    @Test
    void getAll() {
        when(repository.findAll(nullable(Pageable.class))).thenReturn(new PageImpl<>(users));
        var response = service.getAll(null);
        assertEquals(users.stream().map(mapper::toUserDto).toList(), response.getContent());
    }

    @Test
    void getById() throws UserByIdNotFound {
        when(repository.findById(anyLong())).then(invocation -> users.stream().filter(
                user -> user.getId().equals(invocation.getArgument(0))).findFirst());
        assertThrows(UserByIdNotFound.class, () -> service.getById(-123L), "User with id -123 not found!");
        var response = service.getById(users.get(0).getId());
        assertEquals(mapper.toUserDto(users.get(0)), response);
    }

    @Test
    void add() throws UserWithEmailAlreadyExist {
        when(repository.findByEmail(anyString())).then(invocation -> users.stream().filter(
                user -> user.getEmail().equals(invocation.getArgument(0))).findFirst());
        when(repository.save(any(User.class))).then(invocation -> {
            users.add(invocation.getArgument(0));
            return invocation.getArgument(0);
        });
        var userToSave =
                new UserDtoRequest("Krazy", "Eight", users.get(0).getEmail(), "8vjfc4cp2chifwj");
        assertThrows(UserWithEmailAlreadyExist.class, () -> service.add(userToSave),
                     "User with email " + users.get(0).getEmail() + " already exist!"
        );
        userToSave.setEmail("elicia.johnson@hotmail.com");
        var response = service.add(userToSave);
        verify(repository).findByEmail(userToSave.getEmail());
        verify(repository).save(any(User.class));
        assertEquals(userToSave.getEmail(), response.email());
        assertEquals(userToSave.getName(), response.name());
        assertEquals(userToSave.getSurname(), response.surname());
        assertTrue(encoder.matches(userToSave.getPassword(), users.get(3).getPassword()));
    }

    @Test
    void updateUser() {
    }

    @Test
    void delete() {
    }
}