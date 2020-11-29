package pl.vemu.socialApp.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.vemu.socialApp.entities.User;
import pl.vemu.socialApp.entities.UserDTO;
import pl.vemu.socialApp.exceptions.user.UserByIdNotFoundException;
import pl.vemu.socialApp.exceptions.user.UserWithEmailAlreadyExistException;
import pl.vemu.socialApp.managers.UserManager;
import pl.vemu.socialApp.mappers.UserMapper;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserManager manager;

    private final UserMapper mapper;

    private final BCryptPasswordEncoder encoder;

    /*@PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        Optional<User> userFromDb = repository.findByUsername(user.getUsername());
        if (userFromDb.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User doesn't exist");
        if (encoder.matches(user.getPassword(), userFromDb.get().getPassword())) return ResponseEntity.ok().build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong password!");
    }*/

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
        Optional<UserDTO> userByEmail = manager.findByEmail(user.getEmail());
        if (userByEmail.isPresent()) throw new UserWithEmailAlreadyExistException(user.getEmail());
        user.setPassword(encoder.encode(user.getPassword()));
        UserDTO savedUser = mapper.toUserDTO(manager.save(mapper.toUser(user)));
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(uri).body(savedUser);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> putUser(@PathVariable Long id, @RequestBody @Valid @JsonView(UserDTO.Write.class) UserDTO user) throws UserByIdNotFoundException, UserWithEmailAlreadyExistException {
        update(id, user);
        User userEntity = mapper.toUser(user);
        userEntity.setId(id);
        userEntity.setPassword(encoder.encode(user.getPassword()));
        manager.save(userEntity);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/users/{id}") //TODO valid
    public ResponseEntity<?> patchUser(@PathVariable Long id, @RequestBody @JsonView(UserDTO.Write.class) UserDTO user) throws UserByIdNotFoundException, UserWithEmailAlreadyExistException {
        UserDTO userFromDB = update(id, user);
        User mappedUser = mapper.toUserAndCopyNonNullFields(user, userFromDB);
        manager.save(mappedUser);
        return ResponseEntity.noContent().build();
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

    private UserDTO update(Long id, UserDTO user) throws UserByIdNotFoundException, UserWithEmailAlreadyExistException {
        UserDTO userById = manager.findById(id).orElseThrow(() -> new UserByIdNotFoundException(id));
        Optional<UserDTO> userByEmail = manager.findByEmail(user.getEmail());
        if (userByEmail.isPresent() && !userByEmail.get().getEmail().equals(userById.getEmail())) {
            throw new UserWithEmailAlreadyExistException(user.getEmail());
        }
        return userById;
    }
}
