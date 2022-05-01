package pl.vemu.sociely.exceptions.post;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PostByIdNotFound extends Exception {
    public PostByIdNotFound(Long id) {
        super("Post with id " + id + " not found!");
    }
}

