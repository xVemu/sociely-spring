package pl.vemu.sociely.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import pl.vemu.sociely.dtos.request.UserDtoRequest;
import pl.vemu.sociely.entities.User;
import pl.vemu.sociely.exceptions.user.UserByIdNotFound;
import pl.vemu.sociely.exceptions.user.UserWithEmailAlreadyExist;
import pl.vemu.sociely.mappers.UserMapper;
import pl.vemu.sociely.repositories.UserRepository;
import pl.vemu.sociely.utils.Roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private final List<User> users = new ArrayList<>(
            List.of(new User(1L, "Jesse", "Pinkman", "elisabeth.goldner@yahoo.com",
                             "$2a$12$oCXMCYmgaI/cVtUnUvcR3uSqAf6P8O9GQ.5rFjBuQb1Cl4wu5X1eC" /*password: uz2iov8599*/,
                             Roles.USER, Collections.emptyList(), null
            ), new User(2L, "Todd", "Alquist", "randall.muller@yahoo.com",
                        "$2a$12$i8XVImk2GVR25s/JAEhw7OWx5Y8uE9G5AjHcNZSFjkUewbZVETevS" /*password: zt1uk678ydtck7*/,
                        Roles.ADMIN, Collections.emptyList(), null
            ), new User(3L, "Adam", null, "joan.macejkovic@hotmail.com",
                        "$2a$12$eX7ZhRC9Tw7GJTXLxwNcFutWbqSe22No4y6j1qFJoUPSReO9UkUka" /*password: 519kujoju*/,
                        Roles.MODERATOR, Collections.emptyList(), null
            )));
    @Mock
    UserRepository repository;
    @Spy
    BCryptPasswordEncoder encoder;

    @Spy
    UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @InjectMocks
    UserService service;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(mapper, "encoder", encoder);
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

        var userToSave = new UserDtoRequest("Krazy", "Eight", users.get(0).getEmail(), "8vjfc4cp2chifwj");
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
    void updateUser() throws UserByIdNotFound, UserWithEmailAlreadyExist {
        when(repository.findById(anyLong())).then(invocation -> users.stream().filter(
                user -> user.getId().equals(invocation.getArgument(0))).findFirst());
        when(repository.findByEmail(anyString())).then(invocation -> users.stream().filter(
                user -> user.getEmail().equals(invocation.getArgument(0))).findFirst());
        when(repository.save(any(User.class))).then(invocation -> {
            int index = IntStream.range(0, users.size()).filter(i -> users.get(i).getId().equals(
                    ((User) invocation.getArgument(0)).getId())).findFirst().getAsInt();
            users.set(index, invocation.getArgument(0));
            return invocation.getArgument(0);
        });

        var userToUpdate = new UserDtoRequest("Krazy", "Eight", users.get(2).getEmail(), "8vjfc4cp2chifwj");

        assertThrows(
                UserByIdNotFound.class, () -> service.updateUser(-123L, userToUpdate), "User with id -123 not found!");
        assertThrows(UserWithEmailAlreadyExist.class, () -> service.updateUser(1L, userToUpdate),
                     "User with email " + users.get(2).getEmail() + " already exist!"
        );
        userToUpdate.setEmail("jed.carroll@yahoo.com");
        var response = service.updateUser(1L, userToUpdate);
        verify(repository).findByEmail(userToUpdate.getEmail());
        verify(repository).save(any(User.class));
        assertEquals(userToUpdate.getEmail(), response.email());
        assertEquals(userToUpdate.getName(), response.name());
        assertEquals(userToUpdate.getSurname(), response.surname());
        assertTrue(encoder.matches(userToUpdate.getPassword(), users.get(0).getPassword()));

    }

    @Test
    void delete() throws UserByIdNotFound {
        when(repository.findById(anyLong())).then(invocation -> users.stream().filter(
                user -> user.getId().equals(invocation.getArgument(0))).findFirst());
        doAnswer(invocation -> users.removeIf(user -> user.getId().equals(invocation.getArgument(0)))).when(
                repository).deleteById(anyLong());

        var userToDelete = users.get(0);
        service.deleteById(userToDelete.getId());

        assertThrows(UserByIdNotFound.class, () -> service.deleteById(-123L), "User with id -123 not found!");
        assertEquals(2, users.size());
        assertFalse(users.contains(userToDelete));
    }
}