package pl.vemu.sociely.mappers;

import org.mapstruct.*;
import pl.vemu.sociely.dtos.request.PostDtoRequest;
import pl.vemu.sociely.dtos.response.PostDtoResponse;
import pl.vemu.sociely.entities.Post;

@Mapper(componentModel = "spring", uses = UserMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PostMapper {

    Post toPost(PostDtoRequest postDTO);

    @Mapping(target = "creationDate", expression = "java(ZonedDateTime.of(post.getCreationDate(), java.time.ZoneOffset.UTC))")
    PostDtoResponse toPostDTO(Post post);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Post updatePostFromPostDto(PostDtoRequest postDto, @MappingTarget Post post);
}
