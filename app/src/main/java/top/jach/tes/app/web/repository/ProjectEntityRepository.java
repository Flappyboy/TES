package top.jach.tes.app.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.jach.tes.app.web.entity.ProjectEntity;

public interface ProjectEntityRepository extends JpaRepository<ProjectEntity, Long> {
}
