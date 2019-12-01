package top.jach.tes.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.jach.tes.app.entity.ProjectEntity;

public interface ProjectEntityRepository extends JpaRepository<ProjectEntity, Long> {
}
