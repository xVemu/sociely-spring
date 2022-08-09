package pl.vemu.sociely.dtos.response;

import java.time.LocalDateTime;

public record CommentDtoResponse(
        Long id,
        UserDtoResponse user,
        PostDtoResponse post,
        String text,
        LocalDateTime creationDate
) {

}
