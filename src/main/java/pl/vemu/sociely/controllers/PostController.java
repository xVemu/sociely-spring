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
import pl.vemu.sociely.dtos.request.PostDtoRequest;
import pl.vemu.sociely.dtos.response.PostDtoResponse;
import pl.vemu.sociely.entities.User;
import pl.vemu.sociely.exceptions.post.PostByIdNotFound;
import pl.vemu.sociely.exceptions.post.UnauthorizedToManipulatePost;
import pl.vemu.sociely.services.PostService;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService service;

    @GetMapping
    public Page<PostDtoResponse> getPosts(
            @PageableDefault(size = 20) @SortDefault.SortDefaults(@SortDefault(sort = "creationDate", direction = Sort.Direction.ASC)) Pageable pageable,
            @RequestParam Optional<Long> userId
    ) {
        if (userId.isPresent()) return service.getAllByUser(userId.get(), pageable);
        return service.getAll(pageable);
    }

    @GetMapping("{id}")
    public PostDtoResponse getPostById(@PathVariable Long id) throws PostByIdNotFound {
        return service.getById(id);
    }

    @PostMapping
    public ResponseEntity<PostDtoResponse> addPost(
            @RequestBody @Valid PostDtoRequest postDTO,
            @AuthenticationPrincipal User user,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        var savedPost = service.add(postDTO, user);
        URI uri = uriComponentsBuilder.path("/{id}").buildAndExpand(savedPost.id()).toUri();
        return ResponseEntity.created(uri).body(savedPost);
    }

    @PatchMapping("{id}")
    public ResponseEntity<PostDtoResponse> updatePost(
            @PathVariable Long id,
            @RequestBody @Valid PostDtoRequest postDto,
            @AuthenticationPrincipal User user,
            UriComponentsBuilder uriComponentsBuilder
    ) throws UnauthorizedToManipulatePost, PostByIdNotFound {
        var updatedPost = service.update(id, postDto, user);
        return ResponseEntity.created(uriComponentsBuilder.build().toUri()).body(updatedPost);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deletePostById(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) throws UnauthorizedToManipulatePost, PostByIdNotFound {
        service.deleteById(id, user);
        return ResponseEntity.noContent().build();
    }
}
