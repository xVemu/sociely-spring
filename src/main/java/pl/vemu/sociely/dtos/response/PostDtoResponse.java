package pl.vemu.sociely.dtos.response;

import java.time.ZonedDateTime;

public record PostDtoResponse(
        Long id,
        String text,
        ZonedDateTime creationDate,
        UserDtoResponse user
) {
}
