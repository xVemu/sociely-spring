package pl.vemu.sociely.dtos.response;

import java.util.Date;

public record PostDtoResponse(
        Long id,
        String text,
        Date creationDate,
        UserDtoResponse user
) {
}
