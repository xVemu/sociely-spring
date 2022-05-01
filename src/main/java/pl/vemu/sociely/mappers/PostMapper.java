package pl.vemu.sociely.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import pl.vemu.sociely.dtos.request.PostDtoRequest;
import pl.vemu.sociely.dtos.response.PostDtoResponse;
import pl.vemu.sociely.entities.Post;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface PostMapper {

    Post toPost(PostDtoRequest postDTO);

    PostDtoResponse toPostDTO(Post post);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Post updatePostFromPostDto(PostDtoRequest postDto, @MappingTarget Post post);
}
