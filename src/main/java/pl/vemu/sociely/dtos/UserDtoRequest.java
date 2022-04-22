package pl.vemu.sociely.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record UserDtoRequest(
        @NotBlank String name,
        String surname,
        @Email @Size(max = 320) String email,
        @NotBlank String password
) {
}
