package t1.school.tasks.exceptions;

import java.util.NoSuchElementException;

public class NoSuchTaskException extends NoSuchElementException {
    public NoSuchTaskException(Long id) {
        super("Нет задачи с id = " + id);
    }
}
