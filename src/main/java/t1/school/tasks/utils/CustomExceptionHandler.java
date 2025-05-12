package t1.school.tasks.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import t1.school.tasks.dtos.ErrorDTO;
import t1.school.tasks.exceptions.NoSuchTaskException;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(NoSuchTaskException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleNoSuchTaskException(NoSuchTaskException exception) {
        return handleAnyException(exception);
    }

    private ErrorDTO handleAnyException(Exception exception) {
        return new ErrorDTO(exception.getClass().getName(), exception.getMessage());
    }
}
