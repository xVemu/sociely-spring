package pl.vemu.sociely.dtos.response;

import java.time.LocalDateTime;

public record CommentDtoResponse(
        Long id,
        UserDtoResponse user,
        String text,
        LocalDateTime creationDate
) {

}
