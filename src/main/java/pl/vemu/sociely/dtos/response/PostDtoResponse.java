package pl.vemu.sociely.dtos.response;

import java.time.LocalDateTime;

public record PostDtoResponse(
        Long id,
        String text,
        LocalDateTime creationDate,
        UserDtoResponse user
) {
}
