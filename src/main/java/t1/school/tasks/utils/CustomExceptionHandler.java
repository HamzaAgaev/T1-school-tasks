package t1.school.tasks.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import t1.school.tasks.exceptions.NoSuchTaskException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(NoSuchTaskException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNoSuchTaskException(NoSuchTaskException exception) {
        return handleAnyException(exception);
    }

    private Map<String, String> handleAnyException(Exception exception) {
        Map<String, String> map = new HashMap<>();
        map.put("error", exception.getClass().getName());
        map.put("details", exception.getMessage());
        return map;
    }
}
