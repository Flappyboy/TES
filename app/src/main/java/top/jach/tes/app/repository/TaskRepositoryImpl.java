package top.jach.tes.app.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.jach.tes.app.entity.TaskEntity;
import top.jach.tes.core.domain.Task;
import top.jach.tes.core.repository.TaskRepository;

@Component
public class TaskRepositoryImpl implements TaskRepository {
    @Autowired
    TaskEntityRepository taskEntityRepository;
    @Override
    public Task save(Task task) {
        return taskEntityRepository.save(TaskEntity.createFromTask(task)).toTask();
    }
}
