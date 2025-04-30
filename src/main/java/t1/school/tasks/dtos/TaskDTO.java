package t1.school.tasks.dtos;

import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class TaskDTO {
    @Null
    private Long id;
    @NotNull
    private String title;
    private String description;
    @NotNull
    private Long userId;
}
