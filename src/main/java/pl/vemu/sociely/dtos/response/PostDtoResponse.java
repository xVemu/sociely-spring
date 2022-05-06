package pl.vemu.sociely.dtos.response;

import java.time.Instant;

public record PostDtoResponse(
        Long id,
        String text,
        Instant creationDate, /*TODO ZonedDateTime*/
        UserDtoResponse user
) {
}
