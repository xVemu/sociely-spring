package pl.vemu.socialApp.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.*;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User {

    public User(@NonNull String name, String surname, @NonNull String email, @NonNull String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Null
    private Long id;

    @NonNull
    @NotNull
    @NotBlank
    private String name;

    private String surname;

    @NonNull
    @NotNull/*(message = "must be not null")*/
    @Email/*(message = "is not valid mail")*/
    @Size(max = 320)
    @NotBlank
    private String email;

    @NonNull
    @NotNull
    @NotBlank
    private String password;
}