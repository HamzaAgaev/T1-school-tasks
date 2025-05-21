package t1.school.tasks.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import t1.school.tasks.utils.TaskStatus;

@Getter
@Setter
@Entity
@ToString
@Table(name = "tasks")
public class TaskEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String description;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;
    private Long userId;
}
