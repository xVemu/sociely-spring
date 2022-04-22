package pl.vemu.sociely.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

public record UserDtoPatch(
        @Size(min = 2) String name,
        String surname,
        @Email @Size(min = 1, max = 320) String email,
        @Size(min = 1) String password
) {
}
