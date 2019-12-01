package top.jach.tes.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.jach.tes.app.entity.TaskEntity;

public interface TaskEntityRepository extends JpaRepository<TaskEntity, Long> {
}
