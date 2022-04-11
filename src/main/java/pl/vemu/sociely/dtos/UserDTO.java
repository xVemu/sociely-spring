package pl.vemu.sociely.dtos;


import com.fasterxml.jackson.annotation.JsonView;
import lombok.NonNull;
import pl.vemu.sociely.utils.Roles;
import pl.vemu.sociely.utils.View.Normal;
import pl.vemu.sociely.utils.View.Read;
import pl.vemu.sociely.utils.View.Write;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record UserDTO(
        @JsonView(Read.class)
        Long id,

        @NotNull
        @NotBlank
        @JsonView(Normal.class)
        String name,

        @JsonView(Normal.class)
        String surname,

        @NotNull
        @Email
        @Size(max = 320)
        @NotBlank
        @JsonView(Normal.class)
        String email,

        @NotNull
        @NotBlank
        @JsonView(Write.class)
        String password,

        @NotNull
        @NotBlank
        @JsonView(Normal.class)
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
