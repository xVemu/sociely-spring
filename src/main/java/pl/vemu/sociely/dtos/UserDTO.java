package pl.vemu.sociely.dtos;


import lombok.NonNull;
import pl.vemu.sociely.utils.Roles;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record UserDTO(
        Long id,

        @NotNull
        @NotBlank
        String name,

        String surname,

        @NotNull
        @Email
        @Size(max = 320)
        @NotBlank
        String email,

        @NotNull
        @NotBlank
        String password,

        @NotNull
        @NotBlank
        String role
) {
    public UserDTO(@NonNull @NotNull @NotBlank Long id, @NonNull @NotNull @NotBlank String name, String surname, @NonNull @NotNull @Email @Size(max = 320) @NotBlank String email, @NonNull @NotNull @NotBlank String password, @NonNull @NotNull @NotBlank Roles role) {
        this(id, name, surname, email, password, role.getAuthority());
    }

    // TODO is NonNull and NotBlank needed?
    public UserDTO(@NonNull @NotNull @NotBlank String name, String surname, @NonNull @NotNull @Email @Size(max = 320) @NotBlank String email, @NonNull @NotNull @NotBlank String password, @NonNull @NotNull @NotBlank String role) {
        this(null, name, surname, email, password, role);
    }
}
