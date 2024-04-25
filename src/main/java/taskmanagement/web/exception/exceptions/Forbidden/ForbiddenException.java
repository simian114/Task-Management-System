package taskmanagement.web.exception.exceptions.Forbidden;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException{
    public ForbiddenException() {
        super("forbidden");
    }

    public ForbiddenException(String message) {
        super(message);
    }
}
