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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.vemu.sociely.dtos.PostDTO;
import pl.vemu.sociely.entities.Post;
import pl.vemu.sociely.entities.User;
import pl.vemu.sociely.mappers.PostMapper;
import pl.vemu.sociely.services.PostService;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService service;
    private final PostMapper mapper;

    //TODO handle errors

    @GetMapping
    public Page<PostDTO> getPosts(
            @PageableDefault(size = 20)
            @SortDefault.SortDefaults(
                    @SortDefault(sort = "creationDate", direction = Sort.Direction.ASC)
            )
                    Pageable pageable) {
        return service.getPageablePosts(pageable);
    }

    @PostMapping
    public ResponseEntity<PostDTO> addPost(@RequestBody @Valid PostDTO postDTO, @AuthenticationPrincipal User principal) {
        Post newPost = mapper.toPost(postDTO);
        newPost.setUser(principal);
        PostDTO savedPost = service.save(newPost);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPost.getId())
                .toUri();
        return ResponseEntity.created(uri).body(savedPost);
    }

    @DeleteMapping
    public void deletePostById(Long id) {
        service.deleteById(id);
    }
}
