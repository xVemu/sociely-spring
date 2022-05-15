package pl.vemu.sociely.services;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.vemu.sociely.dtos.request.PostDtoRequest;
import pl.vemu.sociely.dtos.response.PostDtoResponse;
import pl.vemu.sociely.entities.User;
import pl.vemu.sociely.exceptions.post.PostByIdNotFound;
import pl.vemu.sociely.exceptions.post.UnauthorizedToManipulatePost;
import pl.vemu.sociely.mappers.PostMapper;
import pl.vemu.sociely.repositories.PostRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository repository;
    private final PostMapper mapper;

    public Page<PostDtoResponse> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toPostDTO);
    }

    public Page<PostDtoResponse> getAllByUser(Long userId, Pageable pageable) {
        return repository.findAllByUserId(userId, pageable).map(mapper::toPostDTO);
    }

    public PostDtoResponse getById(Long id) throws PostByIdNotFound {
        return repository.findById(id).map(mapper::toPostDTO).orElseThrow(() -> new PostByIdNotFound(id));
    }

    public PostDtoResponse add(PostDtoRequest postDto, User user) {
        var mappedPost = mapper.toPost(postDto);
        mappedPost.setUser(user);
        mappedPost.setCreationDate(LocalDateTime.now(ZoneOffset.UTC));
        return mapper.toPostDTO(repository.save(mappedPost));
    }

    public PostDtoResponse update(
            Long id,
            PostDtoRequest postDto,
            User user
    ) throws PostByIdNotFound, UnauthorizedToManipulatePost {
        var postFromDb = repository.findById(id).orElseThrow(() -> new PostByIdNotFound(id));
        if (!postFromDb.getUser().getId().equals(user.getId())) throw new UnauthorizedToManipulatePost(id);

        var updatedPost = mapper.updatePostFromPostDto(postDto, postFromDb);
        var post = repository.save(updatedPost);
        return mapper.toPostDTO(post);
    }

    public void deleteById(Long id, User user) throws PostByIdNotFound, UnauthorizedToManipulatePost {
        var post = repository.findById(id).orElseThrow(() -> new PostByIdNotFound(id));
        if (!post.getUser().getId().equals(user.getId())) throw new UnauthorizedToManipulatePost(id);
        repository.deleteById(post.getId());
    }
}
