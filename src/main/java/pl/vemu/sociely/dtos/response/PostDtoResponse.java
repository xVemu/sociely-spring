package pl.vemu.sociely.dtos.response;

import java.time.LocalDateTime;
import java.util.List;

public record PostDtoResponse(
        Long id,
        String text,
        LocalDateTime creationDate,
        UserDtoResponse user,

        List<CommentDtoResponse> comments
) {
}
