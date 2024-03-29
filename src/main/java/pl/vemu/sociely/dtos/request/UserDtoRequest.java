package pl.vemu.sociely.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import pl.vemu.sociely.utils.PatchValid;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class UserDtoRequest {

    @NotBlank
    @Size(min = 2, groups = {PatchValid.class})
    private String name;

    @Size(min = 2, groups = {PatchValid.class})
    @Size(min = 2)
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
