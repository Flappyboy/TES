package top.jach.tes.app.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.jach.tes.app.web.entity.TaskEntity;

public interface TaskEntityRepository extends JpaRepository<TaskEntity, Long> {
}
