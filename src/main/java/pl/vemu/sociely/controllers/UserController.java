package pl.vemu.sociely.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.vemu.sociely.entities.User;
import pl.vemu.sociely.entities.UserDTO;
import pl.vemu.sociely.exceptions.user.UserByIdNotFoundException;
import pl.vemu.sociely.exceptions.user.UserWithEmailAlreadyExistException;
import pl.vemu.sociely.managers.UserManager;
import pl.vemu.sociely.mappers.UserMapper;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserManager manager;

    private final UserMapper mapper;

    /*@PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        Optional<User> userFromDb = repository.findByUsername(user.getUsername());
        if (userFromDb.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User doesn't exist");
        if (encoder.matches(user.getPassword(), userFromDb.get().getPassword())) return ResponseEntity.ok().build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong password!");
    }*/

//    TODO handle null request-body

    @GetMapping("/users")
    @JsonView(UserDTO.Read.class)
    public Page<UserDTO> getUsers(@PageableDefault(size = 20)
                                  @SortDefault.SortDefaults({
                                          @SortDefault(sort = "id", direction = Sort.Direction.ASC)
                                  }) Pageable pageable) {
        return manager.findAll(pageable);
    }

    @GetMapping("/users/{id}")
    @JsonView(UserDTO.Read.class)
    public UserDTO getUserById(@PathVariable Long id) throws UserByIdNotFoundException {
        return manager.findById(id).orElseThrow(() -> new UserByIdNotFoundException(id));
    }

    @PostMapping("/users")
    @JsonView(UserDTO.Read.class)
    public ResponseEntity<UserDTO> addUser(@RequestBody @Valid @JsonView(UserDTO.Write.class) UserDTO user) throws UserWithEmailAlreadyExistException {
        Optional<UserDTO> userByEmail = manager.findByEmail(user.email());
        if (userByEmail.isPresent()) throw new UserWithEmailAlreadyExistException(user.email());
        User mappedUser = mapper.toUserWithPasswordEncryption(user);
        UserDTO savedUser = manager.save(mappedUser);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.id())
                .toUri();
        return ResponseEntity.created(uri).body(savedUser);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> putUser(@PathVariable Long id, @RequestBody @Valid @JsonView(UserDTO.Write.class) UserDTO user) throws UserByIdNotFoundException, UserWithEmailAlreadyExistException {
        UserDTO userFromDb = getFromDbIfExist(id, user);
        User userEntity = mapper.toUserWithPasswordEncryption(user);
        userEntity.setId(userFromDb.id());
        manager.save(userEntity);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/users/{id}") //TODO valid
    public ResponseEntity<?> patchUser(@PathVariable Long id, @RequestBody @JsonView(UserDTO.Write.class) UserDTO user) throws UserByIdNotFoundException, UserWithEmailAlreadyExistException {
        UserDTO userFromDB = getFromDbIfExist(id, user);
        User mappedUser = mapper.toUserAndCopyNonNullFields(userFromDB, user);
        manager.save(mappedUser);
        return ResponseEntity.noContent().build();
    }

    private UserDTO getFromDbIfExist(Long id, UserDTO user) throws UserByIdNotFoundException, UserWithEmailAlreadyExistException {
        UserDTO userById = manager.findById(id).orElseThrow(() -> new UserByIdNotFoundException(id));
        Optional<UserDTO> userByEmail = manager.findByEmail(user.email());
        if (userByEmail.isPresent() && !userByEmail.get().email().equals(userById.email())) {
            throw new UserWithEmailAlreadyExistException(user.email());
        }
        return userById;
    }

    @DeleteMapping("/users")
    public ResponseEntity<?> deleteUsers() {
        manager.deleteAll();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) throws UserByIdNotFoundException {
        manager.findById(id).orElseThrow(() -> new UserByIdNotFoundException(id));
        manager.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
