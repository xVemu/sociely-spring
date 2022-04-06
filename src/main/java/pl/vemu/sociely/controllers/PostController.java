package pl.vemu.sociely.controllers;

import com.fasterxml.jackson.annotation.JsonView;
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
import pl.vemu.sociely.entities.Post;
import pl.vemu.sociely.entities.User;
import pl.vemu.sociely.entities.dtos.PostDTO;
import pl.vemu.sociely.mappers.PostMapper;
import pl.vemu.sociely.services.PostService;
import pl.vemu.sociely.utils.View.Read;
import pl.vemu.sociely.utils.View.Write;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService manager;
    private final PostMapper mapper;

    //TODO handle errors

    @JsonView(Read.class)
    @GetMapping
    public Page<PostDTO> getPosts(
            @PageableDefault(size = 20)
            @SortDefault.SortDefaults(
                    @SortDefault(sort = "creationDate", direction = Sort.Direction.ASC)
            )
                    Pageable pageable) {
        return manager.findAll(pageable);
    }

    @JsonView(Read.class)
    @PostMapping
    public ResponseEntity<PostDTO> addPost(@RequestBody @Valid @JsonView(Write.class) PostDTO postDTO, @AuthenticationPrincipal User principal) {
        Post newPost = mapper.toPost(postDTO);
        newPost.setUser(principal);
        PostDTO savedPost = manager.save(newPost);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPost.id())
                .toUri();
        return ResponseEntity.created(uri).body(savedPost);
    }

    @DeleteMapping
    public ResponseEntity<?> deletePostById(Long id) {
        manager.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
