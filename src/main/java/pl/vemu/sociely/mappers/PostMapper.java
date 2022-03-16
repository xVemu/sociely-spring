package pl.vemu.sociely.mappers;

import org.mapstruct.*;
import pl.vemu.sociely.entities.Post;
import pl.vemu.sociely.entities.dtos.PostDTO;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(source = "userName", target = "user.name")
    @Mapping(source = "userSurname", target = "user.surname")
    @Mapping(source = "userEmail", target = "user.email")
    @Mapping(source = "userId", target = "user.id")
    Post toPost(PostDTO postDTO);

    @InheritInverseConfiguration(name = "toPost")
    PostDTO toPostDTO(Post post);

    @InheritConfiguration(name = "toPost")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePostFromPostDtoTest(PostDTO postDtoTest, @MappingTarget Post post);
}
