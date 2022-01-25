package pl.vemu.socialapp.exceptions.user;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class UserWithEmailAlreadyExistException extends Exception {
    public UserWithEmailAlreadyExistException(String email) {
        super("User with email " + email + " already exist!");
    }
}
