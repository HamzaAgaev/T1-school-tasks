package t1.school.tasks.mappers;

import org.mapstruct.Mapper;
import t1.school.tasks.dtos.TaskDTO;
import t1.school.tasks.entities.TaskEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskDTO toDTO(TaskEntity entity);
    TaskEntity toEntity(TaskDTO dto);
    List<TaskDTO> toDTOs(List<TaskEntity> entities);
    List<TaskEntity> toEntities(List<TaskDTO> dtos);
}
