package pl.vemu.sociely.exceptions.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserByIdNotFound extends Exception {
    public UserByIdNotFound(Long id) {
        super("User with id " + id + " not found!");
    }
}

