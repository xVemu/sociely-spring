package pl.vemu.sociely.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import pl.vemu.sociely.dtos.UserDtoPatch;
import pl.vemu.sociely.dtos.UserDtoRequest;
import pl.vemu.sociely.dtos.UserDtoResponse;
import pl.vemu.sociely.entities.User;
import pl.vemu.sociely.exceptions.user.UserByIdNotFoundException;
import pl.vemu.sociely.exceptions.user.UserWithEmailAlreadyExistException;
import pl.vemu.sociely.services.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

//    TODO handle null request-body

    @GetMapping
    public Page<UserDtoResponse> getUsers(
            @PageableDefault(size = 20)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "id", direction = Sort.Direction.ASC)
            })
                    Pageable pageable) {
        return service.getAll(pageable);
    }

    @GetMapping("{id}")
    public UserDtoResponse getUserById(@PathVariable Long id) throws UserByIdNotFoundException {
        return service.getById(id);
    }

    @PostMapping
    public ResponseEntity<UserDtoResponse> addUser(@RequestBody @Valid UserDtoRequest userDto, UriComponentsBuilder uriComponentsBuilder) throws UserWithEmailAlreadyExistException {
        var savedUser = service.add(userDto);
        var uri = uriComponentsBuilder
                .path("/{id}")
                .buildAndExpand(savedUser.id())
                .toUri();
        return ResponseEntity.created(uri).body(savedUser);
    }

    @PatchMapping("{id}")
    public ResponseEntity<UserDtoResponse> updateUser(@PathVariable Long id, @RequestBody @Valid UserDtoPatch userDto, UriComponentsBuilder uriComponentsBuilder) throws UserByIdNotFoundException, UserWithEmailAlreadyExistException {
        var updatedUser = service.updateUser(id, userDto);
        return ResponseEntity.created(uriComponentsBuilder.build().toUri()).body(updatedUser);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id, @AuthenticationPrincipal User principal) throws UserByIdNotFoundException {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
