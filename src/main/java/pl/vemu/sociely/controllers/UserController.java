package pl.vemu.sociely.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import pl.vemu.sociely.dtos.request.UserDtoRequest;
import pl.vemu.sociely.dtos.response.UserDtoResponse;
import pl.vemu.sociely.entities.User;
import pl.vemu.sociely.exceptions.user.UserByIdNotFound;
import pl.vemu.sociely.exceptions.user.UserWithEmailAlreadyExist;
import pl.vemu.sociely.services.UserService;
import pl.vemu.sociely.utils.PatchValid;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping
    public Page<UserDtoResponse> getUsers(
            @PageableDefault(size = 20)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "id", direction = Sort.Direction.ASC)
            })
                    Pageable pageable
    ) {
        return service.getAll(pageable);
    }

    @GetMapping("{id}")
    public UserDtoResponse getUserById(@PathVariable Long id) throws UserByIdNotFound {
        return service.getById(id);
    }

    @PostMapping
    public ResponseEntity<UserDtoResponse> addUser(
            @RequestBody @Valid UserDtoRequest userDto,
            UriComponentsBuilder uriComponentsBuilder
    ) throws UserWithEmailAlreadyExist {
        var savedUser = service.add(userDto);
        var uri = uriComponentsBuilder
                .path("/{id}")
                .buildAndExpand(savedUser.id())
                .toUri();
        return ResponseEntity.created(uri).body(savedUser);
    }

    @PatchMapping("{id}")
    public ResponseEntity<UserDtoResponse> updateUser(
            @PathVariable Long id,
            @RequestBody @Validated(PatchValid.class) UserDtoRequest userDto,
            UriComponentsBuilder uriComponentsBuilder
    ) throws UserByIdNotFound, UserWithEmailAlreadyExist {
        var updatedUser = service.updateUser(id, userDto);
        return ResponseEntity.created(uriComponentsBuilder.build().toUri()).body(updatedUser);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteUserById(
            @PathVariable Long id,
            @AuthenticationPrincipal User principal
    ) throws UserByIdNotFound {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
