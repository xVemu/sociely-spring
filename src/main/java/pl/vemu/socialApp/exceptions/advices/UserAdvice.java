package pl.vemu.socialApp.exceptions.advices;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.vemu.socialApp.exceptions.user.UserByIdNotFoundException;
import pl.vemu.socialApp.exceptions.user.UserWithEmailAlreadyExistException;

//@RestControllerAdvice
public class UserAdvice extends ResponseEntityExceptionHandler {

    //    @ExceptionHandler(UserByIdNotFoundException.class)
    public ResponseEntity<Object> userWithIdNotFoundHandler(UserByIdNotFoundException e, WebRequest webRequest) {
        return handleExceptionInternal(e, e.getMessage(), HttpHeaders.EMPTY, HttpStatus.NOT_FOUND, webRequest);
    }

    //    @ExceptionHandler(UserWithEmailAlreadyExistException.class)
    public ResponseEntity<Object> userWithUsernameNotFoundHandler(UserWithEmailAlreadyExistException e, WebRequest webRequest) {
        return handleExceptionInternal(e, e.getMessage(), HttpHeaders.EMPTY, HttpStatus.CONFLICT, webRequest);
    }

}
