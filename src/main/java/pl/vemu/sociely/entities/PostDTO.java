package pl.vemu.sociely.entities;

import com.fasterxml.jackson.annotation.JsonView;
import pl.vemu.sociely.mappers.View.Normal;
import pl.vemu.sociely.mappers.View.Read;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

public record PostDTO(
        @JsonView(Read.class)
        Long id,
        @NotNull
        @NotBlank
        @JsonView(Normal.class)
        String text,
        @NotNull
        @JsonView(Normal.class)
        Date creationDate,

        Long userId,
        String userName,
        String userSurname,
        String userEmail
) {
    public PostDTO(String text, Date creationDate, Long userId, String userName, String userSurname, String userEmail) {
        this(null, text, creationDate, userId, userName, userSurname, userEmail);
    }

    public PostDTO(String text, Date creationDate, Long userId) {
        this(null, text, creationDate, userId, null, null, null);
    }
}
