package pl.vemu.sociely.exceptions.post;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedToManipulatePost extends Exception {
    public UnauthorizedToManipulatePost() {
        super("Unauthorized to manipulate the post");
    }
}
