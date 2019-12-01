package top.jach.tes.core.repository;

import top.jach.tes.core.domain.Task;

public interface TaskRepository extends Repository{

    Task save(Task task);
}
