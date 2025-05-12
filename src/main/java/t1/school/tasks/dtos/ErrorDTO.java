package t1.school.tasks.dtos;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDTO {
    private String error;
    private String details;
}
