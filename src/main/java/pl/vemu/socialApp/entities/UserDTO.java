package pl.vemu.socialApp.entities;


import com.fasterxml.jackson.annotation.JsonView;
import lombok.NonNull;
import lombok.Value;
import pl.vemu.socialApp.mappers.Default;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
public class UserDTO {

    public UserDTO(@NonNull @NotNull @NotBlank String name, String surname, @NonNull @NotNull @Email @Size(max = 320) @NotBlank String email, @NonNull @NotNull @NotBlank String password) {
        this(null, name, surname, email, password);
    }

    @Default
    public UserDTO(Long id, @NotNull @NotBlank String name, String surname, @NotNull @Email @Size(max = 320) @NotBlank String email, @NotNull @NotBlank String password) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }

    public UserDTO() {
        this.id = null;
        this.name = null;
        this.surname = null;
        this.email = null;
        this.password = null;
    }

    @JsonView(Read.class)
    Long id;

    //    @NonNull
    @NotNull
    @NotBlank
    @JsonView(Normal.class)
    String name;

    @JsonView(Normal.class)
    String surname;

    //    @NonNull
    @NotNull
    @Email
    @Size(max = 320)
    @NotBlank
    @JsonView(Normal.class)
    String email;

    //    @NonNull
    @NotNull
    @NotBlank
    @JsonView(Write.class)
    String password;


    public static class Read extends Normal {
    }

    public static class Write extends Normal {
    }

    private static class Normal {
    }
}
