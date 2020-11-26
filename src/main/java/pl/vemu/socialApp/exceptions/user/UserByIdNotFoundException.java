package pl.vemu.socialApp.exceptions.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserByIdNotFoundException extends Exception {
    public UserByIdNotFoundException(Long id) {
        super("User with id " + id + " not found!");
    }
}

