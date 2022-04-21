package pl.vemu.sociely.dtos;

import org.springframework.lang.Nullable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record UserDtoRequest(
        @NotBlank String name,
        @Nullable String surname,
        @Email @Size(max = 320) String email,
        @NotBlank String password
) {
}
