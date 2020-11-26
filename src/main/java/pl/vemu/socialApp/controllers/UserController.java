package pl.vemu.socialApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
import pl.vemu.socialApp.exceptions.user.UserByIdNotFoundException;
import pl.vemu.socialApp.exceptions.user.UserWithEmailAlreadyExistException;
import pl.vemu.socialApp.managers.UserManager;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api")
//@Validated
public class UserController {

    private final UserManager manager;

    private final ObjectMapper jsonMapper;

    private final BCryptPasswordEncoder encoder;

    @Autowired
    public UserController(UserManager manager, ObjectMapper jsonMapper, BCryptPasswordEncoder encoder) {
        this.manager = manager;
        this.jsonMapper = jsonMapper;
        this.encoder = encoder;
    }

    /*@PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        Optional<User> userFromDb = repository.findByUsername(user.getUsername());
        if (userFromDb.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User doesn't exist");
        if (encoder.matches(user.getPassword(), userFromDb.get().getPassword())) return ResponseEntity.ok().build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong password!");
    }*/

    @GetMapping("/users")
    public Page<User> getUsers(@PageableDefault(size = 20)
                               @SortDefault.SortDefaults({
                                       @SortDefault(sort = "id", direction = Sort.Direction.ASC)
                               }) Pageable pageable) {
        return manager.findAll(pageable);
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable Long id) throws UserByIdNotFoundException {
        return manager.findById(id).orElseThrow(() -> new UserByIdNotFoundException(id));
    }

    @PostMapping("/users")
    public ResponseEntity<User> addUser(@RequestBody @Valid User user) throws UserWithEmailAlreadyExistException {
        Optional<User> userByEmail = manager.findByEmail(user.getEmail());
        if (userByEmail.isPresent()) throw new UserWithEmailAlreadyExistException(user.getEmail());
        user.setId(null);
        user.setPassword(encoder.encode(user.getPassword()));
        User savedUser = manager.save(user);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(uri).body(savedUser);
    }

    // TODO if field is missing
    @PutMapping("/users/{id}")
    public ResponseEntity<Void> putUser(@PathVariable Long id, @RequestBody User user) throws UserByIdNotFoundException, UserWithEmailAlreadyExistException {
        update(id, user);
        user.setId(id);
        user.setPassword(encoder.encode(user.getPassword()));
        manager.save(user);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<Void> patchUser(@PathVariable Long id, @RequestBody User user) throws UserByIdNotFoundException, UserWithEmailAlreadyExistException {
        User userById = update(id, user);
        if (user.getPassword() != null) userById.setPassword(encoder.encode(user.getPassword()));
        if (user.getEmail() != null) userById.setEmail(user.getEmail());
        if (user.getName() != null) userById.setName(user.getName());
        if (user.getSurname() != null) userById.setSurname(user.getSurname());
        manager.save(userById);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/users")
    public ResponseEntity<Void> deleteUsers() {
        manager.deleteAll();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) throws UserByIdNotFoundException {
        manager.findById(id).orElseThrow(() -> new UserByIdNotFoundException(id));
        manager.deleteById(id);
        return ResponseEntity.ok().build();
    }

    private User update(Long id, @RequestBody User user) throws UserByIdNotFoundException, UserWithEmailAlreadyExistException {
        User userById = manager.findById(id).orElseThrow(() -> new UserByIdNotFoundException(id));
        Optional<User> userByEmail = manager.findByEmail(user.getEmail());
        if (userByEmail.isPresent() && !userByEmail.get().getEmail().equals(userById.getEmail())) {
            throw new UserWithEmailAlreadyExistException(user.getEmail());
        }
        return userById;
    }
}
