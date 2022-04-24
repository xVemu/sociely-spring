package pl.vemu.sociely.dtos;

import lombok.Data;
import pl.vemu.sociely.utils.PatchValid;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserDtoRequest {

    @NotBlank
    @Size(min = 2, groups = {PatchValid.class})
    private String name;

    private String surname;

    @NotBlank
    @Size(max = 320)
    @Email
    @Email(groups = {PatchValid.class})
    @Size(min = 1, max = 320, groups = {PatchValid.class})
    private String email;

    @NotBlank
    @Size(min = 1, groups = {PatchValid.class})
    private String password;
}
