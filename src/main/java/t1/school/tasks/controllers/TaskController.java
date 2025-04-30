package t1.school.tasks.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import t1.school.tasks.dtos.TaskDTO;
import t1.school.tasks.services.TaskService;

import java.util.List;

@Validated
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDTO addTask(@Valid @RequestBody TaskDTO dto) {
        return taskService.addTask(dto);
    }

    @GetMapping("/{id}")
    public TaskDTO getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @PutMapping("/{id}")
    public TaskDTO updateTaskById(@PathVariable Long id, @Valid @RequestBody TaskDTO dto) {
        return taskService.updateTaskById(id, dto);
    }

    @DeleteMapping("/{id}")
    public TaskDTO deleteTaskById(@PathVariable Long id) {
        return taskService.deleteTaskById(id);
    }


    @GetMapping
    public List<TaskDTO> getAllTasks() {
        return taskService.getAllTasks();
    }
}
