package t1.school.tasks.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class TaskEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String description;
    private Long userId;
}
