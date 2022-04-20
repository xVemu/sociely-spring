package pl.vemu.sociely.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.vemu.sociely.utils.Roles;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {

    private Long id;

    @NotNull
    @NotBlank
    private String text;

    @NotNull
    private Date creationDate;

    private Long userId;
    private String userName;
    private String userSurname;
    private String userEmail;
    private String userRole;

    //TODO annotations
    public PostDTO(Long id, @NotNull @NotBlank String text, @NotNull Date creationDate, Long userId, String userName, String userSurname, String userEmail, Roles userRole) {
        this(id, text, creationDate, userId, userName, userSurname, userEmail, userRole.getAuthority());
    }
}