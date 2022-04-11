package pl.vemu.sociely.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.vemu.sociely.utils.Roles;
import pl.vemu.sociely.utils.View.Normal;
import pl.vemu.sociely.utils.View.Read;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {

    @JsonView(Read.class)
    private Long id;
    @NotNull
    @NotBlank
    @JsonView(Normal.class)
    private String text;
    @NotNull
    @JsonView(Normal.class)
    private Date creationDate;

    private Long userId;
    private String userName;
    private String userSurname;
    private String userEmail;
    private String userRole;

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