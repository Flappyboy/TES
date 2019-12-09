package top.jach.tes.core.api.repository;

import top.jach.tes.core.api.domain.Task;

public interface TaskRepository extends Repository{

    Task save(Task task);
}
