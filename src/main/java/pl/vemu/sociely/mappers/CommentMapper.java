package pl.vemu.sociely.mappers;

import org.mapstruct.*;
import pl.vemu.sociely.dtos.request.CommentDtoRequest;
import pl.vemu.sociely.dtos.response.CommentDtoResponse;
import pl.vemu.sociely.entities.Comment;

@Mapper(uses = {
        UserMapper.class,
        PostMapper.class
}, injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface CommentMapper {

    Comment toComment(CommentDtoRequest commentDto);

    CommentDtoResponse toCommentDto(Comment comment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Comment updateCommentFromCommentDto(
            CommentDtoRequest commentDto,
            @MappingTarget Comment comment
    );
}
