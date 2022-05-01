package pl.vemu.sociely.exceptions.user;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserWithEmailAlreadyExist extends Exception {
    public UserWithEmailAlreadyExist(String email) {
        super("User with email " + email + " already exist!");
    }
}
