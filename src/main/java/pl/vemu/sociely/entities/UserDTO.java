package pl.vemu.sociely.entities;


import com.fasterxml.jackson.annotation.JsonView;
import lombok.NonNull;

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
        String password
) {

    public UserDTO(@NonNull @NotNull @NotBlank String name, String surname, @NonNull @NotNull @Email @Size(max = 320) @NotBlank String email, @NonNull @NotNull @NotBlank String password) {
        this(null, name, surname, email, password);
    }


    public static class Read extends Normal {
    }

    public static class Write extends Normal {
    }

    private static class Normal {
    }
}
