package pl.vemu.sociely.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import pl.vemu.sociely.dtos.request.PostDtoRequest;
import pl.vemu.sociely.entities.Post;
import pl.vemu.sociely.entities.User;
import pl.vemu.sociely.exceptions.post.PostByIdNotFound;
import pl.vemu.sociely.exceptions.post.UnauthorizedToManipulatePost;
import pl.vemu.sociely.mappers.PostMapper;
import pl.vemu.sociely.mappers.PostMapperImpl;
import pl.vemu.sociely.mappers.UserMapper;
import pl.vemu.sociely.repositories.PostRepository;
import pl.vemu.sociely.utils.Roles;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    private final List<Post> posts = new ArrayList<>(
            List.of(new Post(1L, "I'll scout ahead!", LocalDateTime.parse("2007-12-03T10:15:30.00"),
                             new User(3L, "Jesse", "Pinkman", "elisabeth.goldner@yahoo.com",
                                      "$2a$12$oCXMCYmgaI/cVtUnUvcR3uSqAf6P8O9GQ.5rFjBuQb1Cl4wu5X1eC" /*password: uz2iov8599*/,
                                      Roles.USER, null
                             )
            ), new Post(2L, "I suppose you're expecting some inBEARable pun?",
                        LocalDateTime.parse("2022-05-06T17:15:30.00"),
                        new User(2L, "Todd", "Alquist", "randall.muller@yahoo.com",
                                 "$2a$12$i8XVImk2GVR25s/JAEhw7OWx5Y8uE9G5AjHcNZSFjkUewbZVETevS" /*password: uz2iov8599*/,
                                 Roles.ADMIN, null
                        )
            ), new Post(3L, "By my will, this shall be finished!", LocalDateTime.now(),
                        new User(1L, "Adam", null, "joan.macejkovic@hotmail.com",
                                 "$2a$12$eX7ZhRC9Tw7GJTXLxwNcFutWbqSe22No4y6j1qFJoUPSReO9UkUka" /*password: 519kujoju*/,
                                 Roles.MODERATOR, null
                        )
            )));
    @Mock
    PostRepository repository;
    PostMapper mapper;
    PostService service;

    @BeforeEach
    void setUp() {
        mapper = new PostMapperImpl(Mappers.getMapper(UserMapper.class));
        service = new PostService(repository, mapper);
    }

    @Test
    void getAll() {
        when(repository.findAll(nullable(Pageable.class))).thenReturn(new PageImpl<>(posts));

        var response = service.getAll(null);

        assertEquals(posts.stream().map(mapper::toPostDTO).toList(), response.getContent());
    }

    @Test
    void getAllByUser() {
        when(repository.findAllByUserId(anyLong(), nullable(Pageable.class))).then(invocation -> new PageImpl<>(
                posts.stream().filter(post -> post.getUser().getId().equals(invocation.getArgument(0))).toList()));

        var response = service.getAllByUser(posts.get(0).getUser().getId(), null);

        assertEquals(List.of(mapper.toPostDTO(posts.get(0))), response.getContent());


    }

    @Test
    void getById() throws PostByIdNotFound {
        when(repository.findById(anyLong())).then(invocation -> posts.stream().filter(
                post -> post.getId().equals(invocation.getArgument(0))).findFirst());

        assertThrows(PostByIdNotFound.class, () -> service.getById(-123L), "Post with id -123 not found!");

        var response = service.getById(posts.get(0).getId());

        assertEquals(mapper.toPostDTO(posts.get(0)), response);
    }

    @Test
    void add() {
        when(repository.save(any(Post.class))).then(invocation -> {
            posts.add(invocation.getArgument(0));
            return invocation.getArgument(0);
        });

        var user = new User(4L, "Krazy", "Eight", "lecia.effertz@hotmail.com",
                            "$2a$12$uKmJeaGvVOTONTUyQdq0gOY0LWVDuyV9TlVLZonIrHsjQ3q6Mj3oa" /*password: 8vjfc4cp2chifwj*/,
                            Roles.USER, null
        );
        var now = ZonedDateTime.now(ZoneOffset.UTC);
        var postToSave = new PostDtoRequest("Purge the unjust.");
        var response = service.add(postToSave, user);

        verify(repository).save(any(Post.class));
        assertEquals(postToSave.getText(), response.text());
        assertTrue(response.creationDate().compareTo(now) >= 0);
        assertEquals(4L, response.user().id());
    }

    @Test
    void updateUser() throws PostByIdNotFound, UnauthorizedToManipulatePost {
        when(repository.findById(anyLong())).then(invocation -> posts.stream().filter(
                post -> post.getId().equals(invocation.getArgument(0))).findFirst());
        when(repository.save(any(Post.class))).then(invocation -> {
            int index = IntStream.range(0, posts.size()).filter(i -> posts.get(i).getId().equals(
                    ((Post) invocation.getArgument(0)).getId())).findFirst().getAsInt();
            posts.set(index, invocation.getArgument(0));
            return invocation.getArgument(0);
        });

        var postToUpdate = new PostDtoRequest("numbers.waters@yahoo.com");
        var user = posts.get(0).getUser();

        assertThrows(PostByIdNotFound.class, () -> service.update(-123L, postToUpdate, user),
                     "Post with id -123 not found!"
        );

        assertThrows(
                UnauthorizedToManipulatePost.class,
                () -> service.update(posts.get(0).getId(), postToUpdate, posts.get(2).getUser()),
                "Unauthorized to manipulate the post with id " + posts.get(0).getId()
        );

        var response = service.update(posts.get(0).getId(), postToUpdate, user);
        verify(repository, atLeast(1)).findById(posts.get(0).getId());
        verify(repository).save(any(Post.class));
        assertEquals(postToUpdate.getText(), response.text());
    }

    @Test
    void delete() throws PostByIdNotFound, UnauthorizedToManipulatePost {
        when(repository.findById(anyLong())).then(invocation -> posts.stream().filter(
                post -> post.getId().equals(invocation.getArgument(0))).findFirst());
        doAnswer(invocation -> posts.removeIf(post -> post.getId().equals(invocation.getArgument(0)))).when(
                repository).deleteById(anyLong());

        var postToDelete = posts.get(0);

        assertThrows(PostByIdNotFound.class, () -> service.deleteById(-123L, postToDelete.getUser()),
                     "Post with id -123 not found!"
        );
        assertThrows(
                UnauthorizedToManipulatePost.class,
                () -> service.deleteById(postToDelete.getId(), posts.get(2).getUser()),
                "Unauthorized to manipulate the post with id " + postToDelete.getId()
        );

        service.deleteById(postToDelete.getId(), postToDelete.getUser());

        assertEquals(2, posts.size());
        assertFalse(posts.contains(postToDelete));
    }
}