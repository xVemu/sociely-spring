package pl.vemu.socialApp.entities;


import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    public UserDTO(@NonNull @NotNull @NotBlank String name, String surname, @NonNull @NotNull @Email @Size(max = 320) @NotBlank String email, @NonNull @NotNull @NotBlank String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }

    @JsonView(Read.class)
    private Long id;

    @NonNull
    @NotNull
    @NotBlank
    @JsonView(Normal.class)
    private String name;

    @JsonView(Normal.class)
    private String surname;

    @NonNull
    @NotNull
    @Email
    @Size(max = 320)
    @NotBlank
    @JsonView(Normal.class)
    private String email;

    @NonNull
    @NotNull
    @NotBlank
    @JsonView(Write.class)
    private String password;


    public static class Read extends Normal {
    }

    public static class Write extends Normal {
    }

    private static class Normal {
    }
}
