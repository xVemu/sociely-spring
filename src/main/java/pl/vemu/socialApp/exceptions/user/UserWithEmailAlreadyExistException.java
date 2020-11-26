package pl.vemu.socialApp.exceptions.user;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class UserWithEmailAlreadyExistException extends Exception {
    public UserWithEmailAlreadyExistException(String username) {
        super("User with email " + username + " already exist!");
    }
}
