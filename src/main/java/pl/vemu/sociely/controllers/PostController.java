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
import pl.vemu.sociely.dtos.request.CommentDtoRequest;
import pl.vemu.sociely.dtos.request.PostDtoRequest;
import pl.vemu.sociely.dtos.response.CommentDtoResponse;
import pl.vemu.sociely.dtos.response.PostDtoResponse;
import pl.vemu.sociely.entities.User;
import pl.vemu.sociely.exceptions.post.PostByIdNotFound;
import pl.vemu.sociely.exceptions.post.UnauthorizedToManipulatePost;
import pl.vemu.sociely.services.CommentService;
import pl.vemu.sociely.services.PostService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService service;
    private final CommentService commentService;

    @GetMapping
    public Page<PostDtoResponse> getPosts(
            @PageableDefault(size = 20) @SortDefault.SortDefaults(@SortDefault(sort = "creationDate", direction = Sort.Direction.DESC)) Pageable pageable
    ) {
        return service.getAll(pageable);
    }

    @GetMapping("{id}")
    public PostDtoResponse getPostById(@PathVariable Long id) throws PostByIdNotFound {
        return service.getById(id);
    }

    @GetMapping("{postId}/comments")
    public Page<CommentDtoResponse> getCommentsByPost(
            @PageableDefault(size = 20) @SortDefault.SortDefaults(@SortDefault(sort = "creationDate", direction = Sort.Direction.DESC)) Pageable pageable,
            @PathVariable Long postId
    ) {
        return commentService.getByPost(postId, pageable);
    }

    @PostMapping
    public ResponseEntity<PostDtoResponse> addPost(
            @RequestBody @Valid PostDtoRequest postDTO,
            @AuthenticationPrincipal User user,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        var savedPost = service.add(postDTO, user);
        var uri = uriComponentsBuilder.path("/{id}").buildAndExpand(savedPost.id()).toUri();
        return ResponseEntity.created(uri).body(savedPost);
    }

    @PostMapping("{postId}/comments")
    public ResponseEntity<CommentDtoResponse> addComment(
            @RequestBody @Valid CommentDtoRequest commentDto,
            @PathVariable Long postId,
            @AuthenticationPrincipal User user,
            UriComponentsBuilder uriComponentsBuilder
    ) throws PostByIdNotFound {
        var comment = commentService.add(commentDto, postId, user);
        var uri = uriComponentsBuilder.path("/{id}").buildAndExpand(comment.id()).toUri(); //TODO
        return ResponseEntity.created(uri).body(comment);
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
