package pl.vemu.sociely.entities.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import pl.vemu.sociely.utils.Roles;
import pl.vemu.sociely.utils.View.Normal;
import pl.vemu.sociely.utils.View.Read;

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
        String userEmail,
        String userRole
) {

    public PostDTO(String text, Date creationDate, Long userId) {
        this(null, text, creationDate, userId, null, null, null, (String) null);
    }

    //TODO annotations
    public PostDTO(@JsonView(Read.class)
                           Long id, @NotNull
                   @NotBlank
                   @JsonView(Normal.class)
                           String text, @NotNull
                   @JsonView(Normal.class)
                           Date creationDate, Long userId, String userName, String userSurname, String userEmail, Roles userRole) {
        this(id, text, creationDate, userId, userName, userSurname, userEmail, userRole.getAuthority());
    }
}
