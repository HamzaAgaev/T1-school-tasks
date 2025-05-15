package t1.school.tasks.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import t1.school.tasks.aspects.annotations.*;
import t1.school.tasks.dtos.TaskDTO;
import t1.school.tasks.entities.TaskEntity;
import t1.school.tasks.exceptions.NoSuchTaskException;
import t1.school.tasks.kafka.KafkaTaskProducer;
import t1.school.tasks.mappers.TaskMapper;
import t1.school.tasks.repositories.TaskRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final KafkaTaskProducer kafkaTaskProducer;

    @LogStart
    @LogExecutionTime
    @LogEnd
    @LogResult
    @LogException
    public TaskDTO addTask(TaskDTO dto) {
        TaskEntity entity = taskMapper.toEntity(dto);
        return taskMapper.toDTO(taskRepository.save(entity));
    }

    @LogStart
    @LogExecutionTime
    @LogEnd
    @LogResult
    @LogException
    public TaskDTO updateTaskById(Long id, TaskDTO dto) {
        taskRepository.findById(id).orElseThrow(() -> new NoSuchTaskException(id));
        dto.setId(id);
        TaskEntity entity = taskMapper.toEntity(dto);
        kafkaTaskProducer.sendToDefault("tasks", entity);
        return taskMapper.toDTO(taskRepository.save(entity));
    }

    @LogStart
    @LogExecutionTime
    @LogEnd
    @LogResult
    @LogException
    public TaskDTO deleteTaskById(Long id) {
        TaskDTO taskDTO = getTaskById(id);
        taskRepository.deleteById(id);
        return taskDTO;
    }

    @LogStart
    @LogExecutionTime
    @LogEnd
    @LogResult
    @LogException
    public TaskDTO getTaskById(Long id) {
        TaskEntity entity = taskRepository.findById(id).orElseThrow(() -> new NoSuchTaskException(id));
        return taskMapper.toDTO(entity);
    }

    @LogStart
    @LogExecutionTime
    @LogEnd
    @LogResult
    @LogException
    public List<TaskDTO> getAllTasks() {
        return taskMapper.toDTOs(taskRepository.findAll());
    }
}
