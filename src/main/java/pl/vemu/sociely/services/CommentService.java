package pl.vemu.sociely.services;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.vemu.sociely.dtos.request.CommentDtoRequest;
import pl.vemu.sociely.dtos.response.CommentDtoResponse;
import pl.vemu.sociely.entities.User;
import pl.vemu.sociely.exceptions.post.PostByIdNotFound;
import pl.vemu.sociely.mappers.CommentMapper;
import pl.vemu.sociely.repositories.CommentRepository;
import pl.vemu.sociely.repositories.PostRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository repository;
    private final PostRepository postRepository;
    private final CommentMapper mapper;

//    public CommentDtoResponse getAll

    public Page<CommentDtoResponse> getByPost(Long postId, Pageable pageable) {
        return repository.findAllByPostId(postId, pageable).map(mapper::toCommentDto);
    }

    public Page<CommentDtoResponse> getByUser(Long userId, Pageable pageable) {
        return repository.findAllByUserId(userId, pageable).map(mapper::toCommentDto);
    }

    public CommentDtoResponse add(CommentDtoRequest commentDto, Long postId, User user) throws PostByIdNotFound {
        var post = postRepository.findById(postId).orElseThrow(() -> new PostByIdNotFound(postId));
        var comment = mapper.toComment(commentDto);
        comment.setPost(post);
        comment.setUser(user);
        comment.setCreationDate(LocalDateTime.now(ZoneOffset.UTC));
        return mapper.toCommentDto(repository.save(comment));
    }

    /*public PostDtoResponse update(
            Long id,
            PostDtoRequest postDto,
            User user
    ) throws PostByIdNotFound, UnauthorizedToManipulatePost {
        var postFromDb = repository.findById(id).orElseThrow(() -> new PostByIdNotFound(id));
        if (!postFromDb.getUser().getId().equals(user.getId())) throw new UnauthorizedToManipulatePost(id);

        var updatedPost = mapper.updatePostFromPostDto(postDto, postFromDb);
        var post = repository.save(updatedPost);
        return mapper.toPostDTO(post);
    }*/

    /*public void deleteById(Long id, User user) throws PostByIdNotFound, UnauthorizedToManipulatePost {
        var post = repository.findById(id).orElseThrow(() -> new PostByIdNotFound(id));
        if (!post.getUser().getId().equals(user.getId())) throw new UnauthorizedToManipulatePost(id);
        repository.deleteById(post.getId());
    }*/
}
